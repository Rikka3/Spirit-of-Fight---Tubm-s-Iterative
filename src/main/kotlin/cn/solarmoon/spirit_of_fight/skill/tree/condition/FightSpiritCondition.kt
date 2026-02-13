package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class FightSpiritCondition(
    val need: Int = 100
): SkillTreeCondition {

    override fun test(
        player: Player,
        skill: Skill?
    ): Boolean {
        return player.getFightSpirit().value >= need
    }

    override val description: Component
        get() = Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}", need)

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<FightSpiritCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.intRange(0, 100).optionalFieldOf("requirement", 100).forGetter { it.need }
            ).apply(it, ::FightSpiritCondition)
        }
    }

}