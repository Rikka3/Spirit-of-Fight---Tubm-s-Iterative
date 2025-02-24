package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.player.Player
import java.util.concurrent.ConcurrentLinkedQueue

class AnyCondition(
    val conditions: ConcurrentLinkedQueue<SkillTreeCondition>
): SkillTreeCondition {

    override fun test(
        host: Player,
        skill: Skill?
    ): Boolean {
        return conditions.any { it.test(host, skill) }
    }

    override val description: Component
        get() = Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}", *conditions.map { it.description }.toTypedArray())

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<AnyCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                SkillTreeCondition.CODEC.listOf().xmap({ ConcurrentLinkedQueue(it)}, { it.toList() }).fieldOf("conditions").forGetter { it.conditions }
            ).apply(it, ::AnyCondition)
        }
    }

}