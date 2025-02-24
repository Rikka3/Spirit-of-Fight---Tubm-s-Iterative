package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class JumpingCondition(
    val reserveTime: Int = 5
): SkillTreeCondition {

    private var reserve = 0

    override fun test(
        player: Player,
        skill: Skill?
    ): Boolean {
        if (player.jumping) {
            reserve = reserveTime
        }

        if (reserve > 0) reserve--

        return reserve > 0
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<JumpingCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.INT.optionalFieldOf("reserve_time", 5).forGetter { it.reserveTime }
            ).apply(it, ::JumpingCondition)
        }
    }

}