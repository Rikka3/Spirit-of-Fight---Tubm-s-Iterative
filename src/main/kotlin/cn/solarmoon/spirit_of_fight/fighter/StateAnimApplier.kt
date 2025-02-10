package cn.solarmoon.spirit_of_fight.fighter

import cn.solarmoon.spark_core.animation.presets.EntityStates
import cn.solarmoon.spark_core.animation.presets.PlayerStateAnimMachine
import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import net.minecraft.client.player.LocalPlayer
import net.neoforged.bus.api.SubscribeEvent
import ru.nsk.kstatemachine.statemachine.processEventBlocking

object StateAnimApplier {

    /**
     * 防止moveset为0时切到走路会在一瞬间卡出两个状态
     */
    @SubscribeEvent
    private fun transModifier(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        if (player.isLocalPlayer && player is LocalPlayer) {
            if (event.state == EntityStates.Idle && PlayerLocalController.moveRemain > 0 && player.stateMachine != null) {
                player.stateMachine.processEventBlocking(PlayerStateAnimMachine.ResetEvent)
                event.isCanceled = true
            }
        }
    }

}