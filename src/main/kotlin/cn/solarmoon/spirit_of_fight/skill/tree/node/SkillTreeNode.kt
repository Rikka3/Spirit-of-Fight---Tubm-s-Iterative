package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spark_core.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import com.mojang.serialization.MapCodec
import net.minecraft.client.player.Input
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.function.Function

interface SkillTreeNode {

    val conditions: List<SkillTreeCondition>

    val children: List<SkillTreeNode>

    val reserveTime: Int

    val name: MutableComponent?

    val description: MutableComponent?

    val icon: ResourceLocation

    val persistentCondition: Boolean get() = false

    fun nextNode(index: Int) = children.getOrNull(index)

    fun match(player: Player, skill: Skill?) = conditions.all { it.test(player, skill) }

    fun onEntry(player: Player, level: Level, tree: SkillTree): Boolean = true

    fun onActive(tree: SkillTree, index: Int, player: Player, level: Level, preInput: PreInput, input: Input, root: Boolean) {
        tree.step(this, index, player, level, root)
    }

    val registryKey get() = SOFRegistries.SKILL_TREE_NODE_CODEC.getKey(codec) ?: throw NullPointerException("${javaClass.simpleName} 尚未注册其codec")

    val codec: MapCodec<out SkillTreeNode>

    companion object {
        val CODEC = SOFRegistries.SKILL_TREE_NODE_CODEC.byNameCodec()
            .dispatch(
                SkillTreeNode::codec,
                Function.identity()
            )
    }

}