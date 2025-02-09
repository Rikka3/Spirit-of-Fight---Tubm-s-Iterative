package cn.solarmoon.spirit_of_fight.fighter.player

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.event.EntityTurnEvent
import cn.solarmoon.spark_core.event.PlayerRenderAnimInFirstPersonEvent
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.getFlag
import cn.solarmoon.spirit_of_fight.lock_on.LockOnController
import net.minecraft.client.player.LocalPlayer
import net.minecraft.util.Mth
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ViewportEvent
import org.joml.Vector2f

object CameraAdjuster {

    @SubscribeEvent
    private fun renderAnimInFirstPersonWhenAttack(event: PlayerRenderAnimInFirstPersonEvent) {
//        val player = event.player
//        val sc = player.getTypedSkillController<FightSkillController<*>>()
//        event.shouldRender = sc?.isPlaying() == true
    }

}