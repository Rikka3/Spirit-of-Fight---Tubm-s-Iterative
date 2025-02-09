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
    val timeType: String = "skill",
    val activeTime: List<Vec2> = listOf(),
    children: List<SkillComponent> = listOf()
): SkillComponent(children) {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return PreventMoveWithBackComponent(timeType, activeTime, children)
    }

    override fun onActive(): Boolean {
        return true
    }

    override fun onUpdate(): Boolean {
        val time = if (timeType == "anim") registerContext(AnimInstance::class).time else skill.runTime.toDouble()
        if (activeTime.any { time in it.x..it.y } || activeTime.isEmpty()) {
            val player = skill.holder as? Player ?: return true
            if (player.isLocalPlayer) {
                if ((player as LocalPlayer).savedInput.forwardImpulse < 0) {
                    player.deltaMovement = Vec3(0.0, player.deltaMovement.y, 0.0)
                }
            }
        }
        return true
    }

    override fun onEnd(): Boolean {
        return true
    }

    companion object {
        val CODEC: MapCodec<PreventMoveWithBackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.optionalFieldOf("time_type", "skill").forGetter { it.timeType },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime },
                SkillComponent.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children }
            ).apply(it, ::PreventMoveWithBackComponent)
        }
    }

}