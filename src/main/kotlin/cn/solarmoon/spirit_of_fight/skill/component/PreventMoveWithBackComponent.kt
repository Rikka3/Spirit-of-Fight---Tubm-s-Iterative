package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.skill.SkillTimeLine
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.Vec3
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class PreventMoveWithBackComponent(
    val activeTime: List<SkillTimeLine.Stamp> = listOf()
): SkillComponent() {

    override fun onTick() {
        val level = skill.level
        if (!level.isClientSide) return
        if (skill.timeline.match(activeTime)) {
            val player = Minecraft.getInstance().player ?: return
            if (player != skill.holder) return
            if (player.savedInput.forwardImpulse < 0) {
                player.deltaMovement = Vec3(0.0, player.deltaMovement.y, 0.0)
            }
        }
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<PreventMoveWithBackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                SkillTimeLine.Stamp.CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime }
            ).apply(it, ::PreventMoveWithBackComponent)
        }
    }

}