package cn.solarmoon.spirit_of_fight.event

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.skill.StunHandler
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

object CombatEventHandler {

    init {
        NeoForge.EVENT_BUS.register(this)
    }

    private val lastHittingState = mutableMapOf<Int, Boolean>()
    private val comboEndTimeout = mutableMapOf<Int, Long>()

    @SubscribeEvent
    fun onEntityTick(event: EntityTickEvent.Post) {
        val entity = event.entity
        if (entity is IEntityPatch && !entity.level().isClientSide) {
            val isHitting = entity.isHitting
            val level = entity.level() // Get level here

            // Check for recovery stun and apply cooldowns - REMOVED (Handled in JS)
            
            // State transition: Hitting -> Not Hitting (Combo Dropped?)
            if (lastHittingState[entity.id] == true && !isHitting) {
                // Schedule check for combo end
                comboEndTimeout[entity.id] = level.gameTime + 5 // 5 ticks tolerance
            }
            
            // State transition: Not Hitting -> Hitting (Combo Continued)
            if (lastHittingState[entity.id] == false && isHitting) {
                comboEndTimeout.remove(entity.id)
                entity.isRecoveryStun = false // Reset recovery stun if they manage to attack
            }
            
            // Check timeout
            val timeout = comboEndTimeout[entity.id]
            if (timeout != null && level.gameTime > timeout) {
                // Combo ended naturally
                entity.isRecoveryStun = true
                StunHandler.setStunned(entity, 30) // 1.5s Stun
                entity.comboCooldownUntil = level.gameTime + 30 // Global 1.5s Cooldown
                comboEndTimeout.remove(entity.id)
            }
            
            lastHittingState[entity.id] = isHitting
        }
    }

    @SubscribeEvent
    fun onLivingDamage(event: LivingDamageEvent.Post) {
        val entity = event.entity
        if (entity is IEntityPatch && !entity.level().isClientSide) {
            // Apply recovery stun if interrupted while hitting
            if (entity.isHitting) {
                 // Apply 1.5s stun on interruption
                 StunHandler.setStunned(entity, 30, stopAnim = true, isRecovery = true)
            }
        }
    }

}
