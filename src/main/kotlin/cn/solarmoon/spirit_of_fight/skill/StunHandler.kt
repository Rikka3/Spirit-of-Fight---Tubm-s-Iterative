package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.debug.SimpleBlockDebug
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import kotlin.math.max

object StunHandler {

    init {
        NeoForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    private fun onEntityTick(event: net.neoforged.neoforge.event.tick.EntityTickEvent.Pre) {
        val entity = event.entity
        val patch = entity as? IEntityPatch ?: return

        if (patch.stunTicks > 0 || patch.isKnockedDown) {
            if (patch.stunTicks > 0) {
                patch.stunTicks--
                // SimpleBlockDebug.log("Entity ${entity.id} stunned - remaining ticks: ${entity.stunTicks}")
            }
            if (patch.isKnockedDown) {
                SimpleBlockDebug.log("Entity ${entity.id} is currently knocked down")
            }

            // Stop all movement and actions
            if (entity is Player) {
                // If recovery stun, allow specific actions (managed by KeyHandling/SkillTree)
                // determining if "Switch Attack" is being attempted is hard here without key access.
                // However, stopUsingItem() cancels active usage (like shielding, bow).
                // We might want to allow this if it's the Switch Attack skill?
                // For now, consistent stun behavior:
                entity.stopUsingItem()
                entity.hurtMarked = true
            } else if (entity is LivingEntity) {
                entity.hurtMarked = true
                
                // Stop AI navigation and movement (following GrabApplier pattern)
                if (entity is Mob) {
                    entity.navigation.stop() // Stop pathfinding navigation
                    entity.target = null // Clear current target
                    // Also stop looking at things
                    entity.lookControl.setLookAt(entity.position())
                }
                
                // Clear self-movement values
                entity.xxa = 0f
                entity.yya = 0f
                entity.zza = 0f
            }
        } else {
             // Reset recovery status when stun ends
            if (patch.isRecoveryStun) {
                patch.isRecoveryStun = false
            }
        }
    }

    fun setStunned(entity: net.minecraft.world.entity.Entity, ticks: Int, stopAnim: Boolean = true, isRecovery: Boolean = false) {
        val patch = entity as? IEntityPatch ?: return

        // Interruption logic: Stop current animation on main layer if requested
        if (stopAnim && entity is IEntityAnimatable<*>) {
            entity.animController.getMainLayer().stopAnimation(0)
        }

        patch.stunTicks = max(patch.stunTicks, ticks)
        if (isRecovery) {
            patch.isRecoveryStun = true
        } else if (patch.stunTicks > 0 && !isRecovery) {
             // If a normal stun is applied, it overrides recovery stun (harder stun)
             patch.isRecoveryStun = false
        }
        
        SimpleBlockDebug.log("Entity ${entity.id} stunned - now: ${patch.stunTicks} ticks (applied: $ticks, stopAnim: $stopAnim, recovery: $isRecovery)")
        
        // Immediately stop movement input when stunned is applied
        if (ticks > 0 && entity is LivingEntity) {
            entity.hurtMarked = true
            
            if (entity is Mob) {
                entity.navigation.stop()
                entity.target = null
            }
            
            entity.xxa = 0f
            entity.yya = 0f
            entity.zza = 0f
        }
    }

    fun isStunned(entity: net.minecraft.world.entity.Entity): Boolean {
        val patch = entity as? IEntityPatch ?: return false
        return patch.stunTicks > 0
    }

    /**
     * Resets vanilla invulnerability and hurt time to allow combos.
     */
    fun resetHurtTime(entity: net.minecraft.world.entity.Entity) {
        if (entity is LivingEntity) {
            entity.invulnerableTime = 0
            entity.hurtTime = 0
        }
    }
}
