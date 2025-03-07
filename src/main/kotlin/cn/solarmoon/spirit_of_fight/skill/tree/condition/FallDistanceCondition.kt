package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class FallDistanceCondition(
    val distance: Float
): SkillTreeCondition {

    override fun test(
        player: Player,
        skill: Skill?
    ): Boolean {
        return player.fallDistance > distance
    }

    override val description: Component
        get() = Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}", distance)

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<FallDistanceCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.FLOAT.fieldOf("distance").forGetter { it.distance }
            ).apply(it, ::FallDistanceCondition)
        }
    }


}