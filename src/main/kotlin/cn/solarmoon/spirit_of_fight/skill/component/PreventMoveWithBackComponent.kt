package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.registry.common.SparkSkillContext
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.ranges.contains

class PreventMoveWithBackComponent(
    val activeTime: List<Vec2>? = listOf()
): SkillComponent() {

    override fun onTick() {
        val level = skill.level
        val time = skill.blackBoard.require(SparkSkillContext.TIME, this)
        if (!level.isClientSide) return
        if (activeTime == null) return
        if (activeTime.any { time in it.x..it.y } || activeTime.isEmpty()) {
            val player = Minecraft.getInstance().player ?: return
            if (player.savedInput.forwardImpulse < 0) {
                player.deltaMovement = Vec3(0.0, player.deltaMovement.y, 0.0)
            }
        }
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<PreventMoveWithBackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time").forGetter { Optional.ofNullable(it.activeTime) }
            ).apply(it) { PreventMoveWithBackComponent(it.getOrNull()) }
        }
    }

}