package cn.solarmoon.spirit_of_fight.entity.grab

import cn.solarmoon.spark_core.event.OnPreInputExecuteEvent
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

object GrabApplier {

    @SubscribeEvent
    private fun tick(event: EntityTickEvent.Post) {
        val entity = event.entity
        if (entity.grabManager.grabbedBy != null || entity.isHitting) {
            with(entity) {
                if (this is Mob) {
                    navigation.stop() // 停止路径导航
                }
                if (this is LivingEntity) {
                    // 持续清除自主移动
                    xxa = 0f
                    yya = 0f
                    zza = 0f
                }
            }
        }
    }

    @SubscribeEvent
    private fun changeTarget(event: LivingChangeTargetEvent) {
        val entity = event.entity
        if (entity.grabManager.grabbedBy != null || entity.isHitting) {
            event.newAboutToBeSetTarget = null
        }
    }

    @SubscribeEvent
    private fun onPlayerMove(event: MovementInputUpdateEvent) {
        val player = event.entity
        val input = event.input
        if (player.grabManager.grabbedBy != null || player.isHitting) {
            input.forwardImpulse = 0f
            input.leftImpulse = 0f
            input.up = false
            input.down = false
            input.left = false
            input.right = false
            input.jumping = false
            input.shiftKeyDown = false
            (player as? LocalPlayer)?.sprintTriggerTime = -1
            player.swinging = false
        }
    }

    @SubscribeEvent
    private fun onPreInput(event: OnPreInputExecuteEvent.Pre) {
        val entity = event.preInput.holder as? Entity ?: return
        if (entity.grabManager.grabbedBy != null) {
            event.isCanceled = true
        }
    }

}