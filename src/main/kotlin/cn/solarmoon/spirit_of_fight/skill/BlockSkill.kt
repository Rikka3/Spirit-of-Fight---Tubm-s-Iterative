package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.animation.anim.play.layer.AnimLayerData
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.entity.addRelativeMovement
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillEvent
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spark_core.skill.read
import cn.solarmoon.spark_core.skill.readNonNull
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import org.mozilla.javascript.NativeArray

class BlockSkill: Skill() {
    class Hurt(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()
    class PrecisionBlock(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()
    class Break(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()

    lateinit var guardBody: PhysicsCollisionObject
    var blockReady = false
    var firstPrecisionBlock = true

    init {
        onEvent<SkillEvent.Init> {
            val animatable = holder as? IEntityAnimatable<*> ?: return@onEvent
            val entity = animatable.animatable
            val animPath = config.readNonNull<String>("anim_path").split("/")
            val hurtAnim = AnimInstance.create(animatable, AnimIndex(ResourceLocation.parse(animPath.first()), "${animPath.last()}.hurt")).apply {
                EntityHitApplier.hitAnimDoFreeze(this)
            }
            val breakAnim = AnimInstance.create(animatable, AnimIndex(ResourceLocation.parse(animPath.first()), "${animPath.last()}.break")).apply {
                EntityHitApplier.hitAnimDoFreeze(this)
            }

            onEvent<SkillEvent.ActiveStart> {
                guardBody = config.readNonNull("block_body")
            }

            hurtAnim.onEvent<AnimEvent.Completed> {
            }

            val hurtMovement = config.read("hurt_movement", NativeArray(arrayOf(0.0, 0.0, 1.0))).toVec3()

            onEvent<SkillEvent.Hurt> {
                if (!entity.isGuardEnabled) return@onEvent
                val event = it.event
                val sourcePos = event.source.sourcePosition ?: return@onEvent
                val attacker = event.source.entity ?: return@onEvent
                if (!entity.canSee(sourcePos, config.read("block_range", 150.0))) return@onEvent
                SkillHelper.guard(it.event.source, guardBody) { body, hitPos ->
                    if (blockReady) {
                        if (firstPrecisionBlock) triggerEvent(PrecisionBlock(event, hitPos))
                        firstPrecisionBlock = false
                        it.event.isCanceled = true
                    } else if (hurtAnim.isCancelled) {
                        val fs = entity.getFightSpirit()
                        val consume = event.source.extraData.read(EntityHitApplier.HIT_TYPE)?.fsDamage ?: 25
                        fs.removeStage(consume)
                        fs.syncToClient(entity.id)
                        if (!fs.isEmpty) {
                            hurtAnim.refresh()
                            animatable.animController.getMainLayer().setAnimation(hurtAnim, AnimLayerData(enterTransitionTime = 0))
                            entity.addRelativeMovement(sourcePos, hurtMovement)
                            if (entity is ServerPlayer) entity.connection.send(ClientboundSetEntityMotionPacket(entity))
                            PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag().apply {
                                putBoolean("hurt", true)
                            }))
                            triggerEvent(Hurt(event, hitPos))
                            it.event.isCanceled = true
                        } else if (breakAnim.isCancelled) {
                            breakAnim.refresh()
                            animatable.animController.getMainLayer().setAnimation(breakAnim, AnimLayerData(enterTransitionTime = 0))
                            entity.addRelativeMovement(sourcePos, hurtMovement)
                            if (entity is ServerPlayer) entity.connection.send(ClientboundSetEntityMotionPacket(entity))
                            triggerEvent(Break(event, hitPos))
                            PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag()))
                        }
                    }
                }
            }

            onEvent<SkillEvent.Sync> { e ->
                val data = e.data
                if (data.getBoolean("hurt")) {
                    hurtAnim.refresh()
                    animatable.animController.getMainLayer().setAnimation(hurtAnim, AnimLayerData(enterTransitionTime = 0))
                } else {
                    breakAnim.refresh()
                    animatable.animController.getMainLayer().setAnimation(breakAnim, AnimLayerData(enterTransitionTime = 0))
                }
            }

            onEvent<SkillEvent.End> {
                entity.removeBody(guardBody.name)
            }

            onEvent<SkillEvent.Active> {
                blockReady = false
            }
        }
    }

    fun readyPrecisionBlock() {
        blockReady = true
    }

}