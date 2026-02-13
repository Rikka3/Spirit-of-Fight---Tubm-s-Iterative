package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.layer.AnimLayerData
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spirit_of_fight.debug.SimpleBlockDebug
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spark_core.skill.read
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spirit_of_fight.skill.BlockSkill
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.jme3.math.Vector3f
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.bus.api.EventPriority
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerPlayer
import cn.solarmoon.spirit_of_fight.sync.BlockBreakProgressPayload
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

object BlockDamageHandler {

    private val damageBySource = mutableMapOf<Int, MutableMap<Any, Float>>() // Entity ID -> Damage Source -> Total Damage

    init {
        NeoForge.EVENT_BUS.register(this)
    }
    
    /**
     * Clears the damage tracking for a specific entity
     */
    fun clearDamageTracking(entityId: Int) {
        damageBySource.remove(entityId)
        SimpleBlockDebug.log("Damage tracking cleared for entity $entityId")
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private fun onIncomingDamage(event: LivingIncomingDamageEvent) {
        SimpleBlockDebug.log("LIVING INCOMING DAMAGE EVENT FIRED!!!")
        val victim = event.entity
        if (victim !is IEntityAnimatable<*>) {
            SimpleBlockDebug.log("Victim is not IEntityAnimatable")
            return
        }

        val animatable = victim as IEntityAnimatable<*> ?: return
        val entity = animatable.animatable

        val attacker = event.source.entity
        if (attacker == null) {
            SimpleBlockDebug.log("No attacker entity")
            return
        }

        val blockSkill = entity.getActiveBlockSkill() as? BlockSkill

        if (blockSkill == null) {
            SimpleBlockDebug.log("No active BlockSkill found")
            return
        }

        if (!blockSkill.isActivated) {
            SimpleBlockDebug.log("BlockSkill not activated")
            return
        }
        
        // Get or create damage tracking map for this entity
        val entityDamageMap = damageBySource.getOrPut(entity.id) { mutableMapOf() }
        // Track damage per source type (using damage source as key)
        val sourceKey = event.source.msgId // Using message id to uniquely identify damage source type
        
        if (!blockSkill.isInPrecisionWindow()) {
            // Regular block - apply knockback to victim
            if (entity.isGuardEnabled && attacker is LivingEntity) {
                // Accumulate damage for this source
                val currentDamage = entityDamageMap.getOrDefault(sourceKey, 0.0f)
                val newDamage = currentDamage + event.amount
                entityDamageMap[sourceKey] = newDamage
                
                // Update block breaking data for the entity
                val blockBreakingData = entity.getBlockBreakingData()
                val oldProgress = blockBreakingData.getProgress()
                
                blockBreakingData.damage = newDamage
                blockBreakingData.isBreaking = true
                
                SimpleBlockDebug.log("Damage from source ${event.source.msgId}: $newDamage/20.0 (10 hearts)")
                
                // Sync block break progress to client
                if (entity is ServerPlayer) {
                    val payload = BlockBreakProgressPayload.fromData(entity.id, blockBreakingData)
                    PacketDistributor.sendToPlayer(entity, payload)
                }
                
                if (newDamage >= 20.0f) { // 10 hearts damage
                    SimpleBlockDebug.log("Block break triggered - damage from single source reached 10 hearts (${newDamage} >= 20.0)")
                    
                    // Use the new handleBlockBreak method
                    blockSkill.handleBlockBreak(animatable, event)
                    
                    event.isCanceled = true
                } else {
                    val knockbackStrength = 0.8
                    val knockbackDirection = entity.getViewVector(1.0f).scale(-knockbackStrength).add(0.0, 0.075, 0.0)
                    
                    entity.deltaMovement = knockbackDirection
                    entity.hurtMarked = true
                    
                    if (entity is ServerPlayer) {
                        entity.connection.send(ClientboundSetEntityMotionPacket(entity))
                    }
                    
                    SimpleBlockDebug.log("Regular block: applying knockback to victim (strength: $knockbackStrength, damage from source ${event.source.msgId}: $newDamage/20.0)")
                    event.isCanceled = true
                }
            } else {
                SimpleBlockDebug.log("Regular block failed - entity.isGuardEnabled: ${entity.isGuardEnabled}, attacker is LivingEntity: ${attacker is LivingEntity}")
            }
        } else {
            // Precision block - stun attacker and consume spirit
            SimpleBlockDebug.log("PRECISION BLOCK DETECTED! Active tick: ${blockSkill.getCurrentTick()}, Window: ${blockSkill.getPrecisionWindow()}")
            
            if (entity.isGuardEnabled) {
                val fs = victim.getFightSpirit()
                val consume = event.source.extraData.read(EntityHitApplier.HIT_TYPE)?.fsDamage ?: 25

                SimpleBlockDebug.logSpirit("Spirit consumption triggered")

                if (!fs.isEmpty) {
                    fs.removeStage(consume)
                    fs.syncToClient(victim.id)

                    blockSkill.hurtAnim.refresh()
                    animatable.animController.getMainLayer().setAnimation(blockSkill.hurtAnim, AnimLayerData(enterTransitionTime = 0))
                    val hitPos = victim.position()
                    blockSkill.triggerEvent(BlockSkill.PrecisionBlock(event, hitPos))
                    event.isCanceled = true
                    
                    StunHandler.setStunned(attacker, 40)
                    SimpleBlockDebug.log("Precision block: stunning attacker ${attacker.id} for 40 ticks")
                } else if (blockSkill.breakAnim.isCancelled) {
                    SimpleBlockDebug.log("Block break triggered - insufficient spirit")
                    blockSkill.breakAnim.refresh()
                    animatable.animController.getMainLayer().setAnimation(blockSkill.breakAnim, AnimLayerData(enterTransitionTime = 0))
                    val hitPos = victim.position()
                    blockSkill.triggerEvent(BlockSkill.Break(event, hitPos))
                    
                    // Reset block breaking data
                    val blockBreakingData = entity.getBlockBreakingData()
                    blockBreakingData.reset()
                    if (entity is ServerPlayer) {
                        val payload = BlockBreakProgressPayload.fromData(entity.id, blockBreakingData)
                        PacketDistributor.sendToPlayer(entity, payload)
                    }
                    
                    event.isCanceled = true
                }
            } else {
                SimpleBlockDebug.logCollision("Guard collision", false)
            }
        }
    }
}
