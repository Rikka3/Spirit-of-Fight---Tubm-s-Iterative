package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillHost
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class ReverseCondition(
    val condition: SkillTreeCondition
): SkillTreeCondition {

    override fun test(
        host: Player,
        skill: Skill?
    ): Boolean {
        return !condition.test(host, skill)
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<ReverseCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                SkillTreeCondition.CODEC.fieldOf("condition").forGetter { it.condition }
            ).apply(it, ::ReverseCondition)
        }
    }

}