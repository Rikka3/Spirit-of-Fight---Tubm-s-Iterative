package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spark_core.preinput.PreInput
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import net.minecraft.client.player.Input
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

interface SkillTreeInputNode: SkillTreeNode {

    val preInputId: String

    val preInputDuration: Int

    override fun onActive(
        tree: SkillTree,
        index: Int,
        player: Player,
        level: Level,
        preInput: PreInput,
        input: Input,
        root: Boolean
    ) {
        preInput.setInput(preInputId, preInputDuration) {
            super.onActive(tree, index, player, level, preInput, input, root)
        }
    }

}