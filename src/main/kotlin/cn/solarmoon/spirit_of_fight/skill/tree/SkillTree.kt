package cn.solarmoon.spirit_of_fight.skill.tree

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import cn.solarmoon.spirit_of_fight.sync.MoveDirectionPayload
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.PacketDistributor

class SkillTree(
    val ingredient: Ingredient,
    val rootNode: SkillTreeNode,
    val priority: Int = 0
) {

    private val path: MutableList<Int> = mutableListOf()

    var currentSkill: Skill? = null
    var currentNode: SkillTreeNode? = null
        private set
    var reserveTime = 0
        private set
    private var inputCooldown = 0

    fun tryAdvance(player: Player, input: Input): Boolean {
        val cNode = currentNode
        val level = player.level()
        val preInput = player.getPreInput()

        if (!player.isPlayingSkill) preInput.executeIfPresent()

        val ps = currentSkill
        if (ps != null && !ps.isActive) {
            if (reserveTime > 0) reserveTime--
            else reset()
        }

        // 防连击缓冲
        if (inputCooldown > 0) {
            inputCooldown--
            return false
        }

        // 阶段1：首次触发根节点
        if (cNode == null) {
            if (rootNode.match(player, currentSkill)) {
                activateNode(rootNode, -1, player, level, preInput, input)
                return true
            }
        }
        // 阶段2：触发子节点
        else {
            cNode.children.forEachIndexed { index, child ->
                if (child.match(player, currentSkill)) {
                    activateNode(child, index, player, level, preInput, input)
                    return true
                }
            }
        }

        return false
    }

    private fun activateNode(
        node: SkillTreeNode,
        index: Int,
        player: Player,
        level: Level,
        preInput: PreInput,
        input: Input
    ) {
        preInput.setInput(node.preInputId, node.preInputDuration) {
            inputCooldown = 1

            reserveTime = node.reserveTime

            // 根节点特殊处理（index = -1 表示根节点）
            if (index == -1) {
                currentNode = rootNode
            } else {
                path.add(index)
                currentNode = currentNode?.children?.getOrNull(index)
            }

            // 存储移动方向信息
            val direction = MoveDirection.getByInput((player as LocalPlayer).savedInput)
            player.moveDirection = direction
            PacketDistributor.sendToServer(MoveDirectionPayload(direction?.ordinal ?: -1))

            currentNode?.onEntry(player, level, this)
        }
    }

    fun reset() {
        path.clear()
        reserveTime = 0
        currentSkill = null
        currentNode = null
    }

    companion object {
        val CODEC: Codec<SkillTree> = RecordCodecBuilder.create {
            it.group(
                Ingredient.CODEC.fieldOf("item").forGetter { it.ingredient },
                SkillTreeNode.CODEC.fieldOf("node").forGetter { it.rootNode },
                Codec.INT.optionalFieldOf("priority", 0).forGetter { it.priority }
            ).apply(it, ::SkillTree)
        }
    }

}