package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.mojang.serialization.MapCodec
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import java.util.function.Function

interface SkillTreeCondition {

    fun test(player: Player, skill: Skill?): Boolean

    val codec: MapCodec<out SkillTreeCondition>

    val registryKey: ResourceLocation? get() = SOFRegistries.SKILL_TREE_CONDITION_CODEC.getKey(codec)

    val description: Component get() = registryKey?.let { Component.translatable("skill_tree_condition.${it.namespace}.${it.path}") }
        ?: Component.literal(javaClass.simpleName)

    companion object {
        val CODEC = SOFRegistries.SKILL_TREE_CONDITION_CODEC.byNameCodec()
            .dispatch(
                SkillTreeCondition::codec,
                Function.identity()
            )
    }

}