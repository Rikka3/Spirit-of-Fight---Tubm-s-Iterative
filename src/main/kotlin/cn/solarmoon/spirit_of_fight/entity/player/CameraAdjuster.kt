package cn.solarmoon.spirit_of_fight.entity.player

import cn.solarmoon.spark_core.event.PlayerRenderAnimInFirstPersonEvent
import net.neoforged.bus.api.SubscribeEvent

object CameraAdjuster {

    @SubscribeEvent
    private fun renderAnimInFirstPersonWhenAttack(event: PlayerRenderAnimInFirstPersonEvent) {
//        val player = event.player
//        val sc = player.getTypedSkillController<FightSkillController<*>>()
//        event.shouldRender = sc?.isPlaying() == true
    }

}