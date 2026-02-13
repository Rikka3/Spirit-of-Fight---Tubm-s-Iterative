package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.animation.anim.play.layer.AnimLayerData
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.entity.addRelativeMovement
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillEvent
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spark_core.skill.read
import cn.solarmoon.spark_core.skill.readNonNull
import cn.solarmoon.spirit_of_fight.debug.SimpleBlockDebug
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.math.Vector3f
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import cn.solarmoon.spirit_of_fight.sync.BlockBreakProgressPayload
import org.mozilla.javascript.NativeArray

class BlockSkill: Skill() {
    class Hurt(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()
    class PrecisionBlock(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()
    class Break(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()

    lateinit var guardBody: PhysicsCollisionObject
    lateinit var blockPose: AnimInstance
    lateinit var hurtAnim: AnimInstance
    lateinit var breakAnim: AnimInstance
    
    var totalBlockedDamage = 0.0f
    private var activeTick = 0
    private val precisionWindow = 5 // 5 ticks for precision block (0.25 seconds)
    var isBreaking = false // Track if we're in the break state

    init {
        onEvent<SkillEvent.Init> {
            val animatable = holder as? IEntityAnimatable<*> ?: return@onEvent
            val entity = animatable.animatable
            
            SimpleBlockDebug.log("BlockSkill initializing for entity ${entity.id}")
            
            val animPathRaw = config.readNonNull<String>("anim_path")
            val lastSlash = animPathRaw.lastIndexOf('/')
            val setLocationStr = animPathRaw.substring(0, lastSlash)
            val animName = animPathRaw.substring(lastSlash + 1)
            
            val setLocation = ResourceLocation.parse(setLocationStr)
            
            SimpleBlockDebug.logAnimation("Loading animations", animName)

            blockPose = AnimInstance.create(animatable, AnimIndex(setLocation, animName))
            try {
                hurtAnim = AnimInstance.create(animatable, AnimIndex(setLocation, "${animName}.hurt")).apply {
                    EntityHitApplier.hitAnimDoFreeze(this)
                }
                SimpleBlockDebug.logAnimation("Hurt animation loaded", "hurt")
            } catch (e: Exception) {
                SimpleBlockDebug.logError("Hurt animation not found", "Using block animation as fallback")
                // Use block animation as fallback
                hurtAnim = blockPose
            }
            
            // Try to create break animation, but don't fail if it doesn't exist
            try {
                breakAnim = AnimInstance.create(animatable, AnimIndex(setLocation, "${animName}.break")).apply {
                    EntityHitApplier.hitAnimDoFreeze(this)
                }
                SimpleBlockDebug.logAnimation("Break animation loaded", "break")
            } catch (e: Exception) {
                SimpleBlockDebug.logError("Break animation not found", "Using hurt animation as fallback")
                // Use hurt animation as fallback
                breakAnim = hurtAnim
            }

            onEvent<SkillEvent.ActiveStart> {
                SimpleBlockDebug.log("Block activation started for entity ${entity.id}")
                
                try {
                    guardBody = config.readNonNull("block_body")
                    SimpleBlockDebug.logPhysics("Guard body created", true)
                    SimpleBlockDebug.logAnimation("Starting block animation", "block")
                    animatable.animController.getMainLayer().setAnimation(blockPose)
                    
                    entity.isGuardEnabled = true
                    activeTick = 0
                    totalBlockedDamage = 0.0f
                    entity.setActiveBlockSkill(this)
                    
                    SimpleBlockDebug.logState(entity.id, "BLOCK_ACTIVATED")
                    
                } catch (e: Exception) {
                    SimpleBlockDebug.logError("Block activation", e.message ?: "Unknown error")
                    throw e
                }
            }

            hurtAnim.onEvent<AnimEvent.Completed> {
                SimpleBlockDebug.logAnimation("Hurt animation completed", "hurt")

                if (entity.isGuardEnabled && isActivated && !isBreaking) {
                    SimpleBlockDebug.logAnimation("Transitioning to block animation", "block")
                    animatable.animController.getMainLayer().setAnimation(blockPose)
                }
            }

            breakAnim.onEvent<AnimEvent.Completed> {
                SimpleBlockDebug.logAnimation("Break animation completed", "break")
                
                // After break animation completes, fully end the skill
                if (isBreaking) {
                    SimpleBlockDebug.log("Ending block skill after break animation")
                    end()
                }
            }

            onEvent<SkillEvent.Sync> { e ->
                val data = e.data
                val isHurt = data.getBoolean("hurt")
                
                SimpleBlockDebug.logAnimation("Animation sync event", if (isHurt) "hurt" else "break")
                
                if (isHurt) {
                    hurtAnim.refresh()
                    animatable.animController.getMainLayer().setAnimation(hurtAnim, AnimLayerData(enterTransitionTime = 0))
                } else {
                    breakAnim.refresh()
                    animatable.animController.getMainLayer().setAnimation(breakAnim, AnimLayerData(enterTransitionTime = 0))
                }
            }

            onEvent<SkillEvent.End> {
                SimpleBlockDebug.log("Block deactivation started")
                
                try {
                    entity.removeBody(guardBody.name)
                    SimpleBlockDebug.logPhysics("Guard body removed", true)
                    
                    entity.isGuardEnabled = false
                    entity.setActiveBlockSkill(null)
                    isBreaking = false // Reset breaking state
                    SimpleBlockDebug.logAnimation("Stopping animations", "all")
                    animatable.animController.getMainLayer().stopAnimation(5)
                    
                    SimpleBlockDebug.logState(entity.id, "BLOCK_DEACTIVATED")
                    
                    // Clear damage tracking for this entity
                    BlockDamageHandler.clearDamageTracking(entity.id)
                    
                    // Reset block breaking data
                    entity.getBlockBreakingData().reset()
                    
                    // Sync reset to client
                    if (entity is ServerPlayer) {
                        val payload = BlockBreakProgressPayload.fromData(entity.id, entity.getBlockBreakingData())
                        PacketDistributor.sendToPlayer(entity, payload)
                    }
                } catch (e: Exception) {
                    SimpleBlockDebug.logError("Block deactivation", e.message ?: "Unknown error")
                    throw e
                }
            }

            onEvent<SkillEvent.Active> {
                activeTick++
            }
        }
    }

    fun isInPrecisionWindow(): Boolean {
        return activeTick <= precisionWindow
    }

    fun getCurrentTick(): Int {
        return activeTick
    }
    
    fun getPrecisionWindow(): Int {
        return precisionWindow
    }

    /**
     * Handles the block break event
     * This method is called when the block is broken by taking 10 hearts of damage
     */
    fun handleBlockBreak(animatable: IEntityAnimatable<*>, event: LivingIncomingDamageEvent) {
        val entity = animatable.animatable
        
        SimpleBlockDebug.log("Block break triggered for entity ${entity.id}")
        
        // Set breaking state
        isBreaking = true
        
        // Play break animation
        breakAnim.refresh()
        animatable.animController.getMainLayer().setAnimation(breakAnim, AnimLayerData(enterTransitionTime = 0))
        SimpleBlockDebug.logAnimation("Playing break animation", "break")
        
        // Trigger break event
        val hitPos = entity.position()
        triggerEvent(Break(event, hitPos))
        
        // Stun the player for 3 seconds (60 ticks)
        StunHandler.setStunned(entity, 60)
        SimpleBlockDebug.log("Stunned entity ${entity.id} for 60 ticks")
        
        // Clean up block state manually
        try {
            entity.removeBody(guardBody.name)
            SimpleBlockDebug.logPhysics("Guard body removed during break", true)
            
            entity.isGuardEnabled = false
            entity.setActiveBlockSkill(null)
            SimpleBlockDebug.logState(entity.id, "BLOCK_BROKEN")
            
            // Clear damage tracking for this entity
            BlockDamageHandler.clearDamageTracking(entity.id)
            
            // Reset block breaking data
            entity.getBlockBreakingData().reset()
            
            // Sync reset to client
            if (entity is ServerPlayer) {
                val payload = BlockBreakProgressPayload.fromData(entity.id, entity.getBlockBreakingData())
                PacketDistributor.sendToPlayer(entity, payload)
            }
        } catch (e: Exception) {
            SimpleBlockDebug.logError("Block break cleanup", e.message ?: "Unknown error")
        }
    }

}