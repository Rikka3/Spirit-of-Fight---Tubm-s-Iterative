package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class PreventMoveWithBackComponent(
    val activeTime: List<Vec2> = listOf()
): SkillComponent() {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return PreventMoveWithBackComponent(activeTime)
    }

    override fun onActive() {}

    override fun onUpdate() {
        val time = requireQuery<() -> Double>("time").invoke()
        if (activeTime.any { time in it.x..it.y } || activeTime.isEmpty()) {
            val player = skill.holder as? Player ?: return
            if (player.isLocalPlayer) {
                if ((player as LocalPlayer).savedInput.forwardImpulse < 0) {
                    player.deltaMovement = Vec3(0.0, player.deltaMovement.y, 0.0)
                }
            }
        }
    }

    override fun onEnd() {}

    companion object {
        val CODEC: MapCodec<PreventMoveWithBackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime }
            ).apply(it, ::PreventMoveWithBackComponent)
        }
    }

}