package cn.solarmoon.spirit_of_fight.skill.tree.ui

import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import net.minecraft.client.gui.Font
import org.joml.Vector2i
import org.lwjgl.util.freetype.PS_Private
import kotlin.math.max

class SkillTreeLayout(
    private val tree: SkillTree,
    private val font: Font,
    private val verticalDrop: Int // 节点下方垂直线长度
) {

    val nodePositions = linkedMapOf<SkillTreeNode, Pair<Int, Int>>().apply { positionNodes(this, tree.rootNode, 0 to 0) }
    val nodeSpacePositions = linkedMapOf<SkillTreeNode, Pair<Int, Int>>().apply { positionNodes(this, tree.rootNode, 0 to 0) }
    val iconWidth = 64
    val iconHeight = 64
    val conditionOffset = 8
    val totalWidth = calculateTreeWidth(tree.rootNode)
    val totalHeight = calculateTreeHeight(tree.rootNode, 0)

    val spacePos: Vector2i
        get() {
            val o = nodeSpacePositions[tree.rootNode]!!
            return Vector2i(o.first, o.second)
        }

    var pos: Vector2i
        set(value) {
            positionNodes(nodePositions, tree.rootNode, value.x to value.y)
        }
        get() {
            val o = nodePositions[tree.rootNode]!!
            return Vector2i(o.first, o.second)
        }

    private fun calculateTreeWidth(node: SkillTreeNode): Int {
        // 当前节点自身需要的宽度
        val selfWidth = maxOf(
            font.width(node.name),
            (node.conditions.maxOfOrNull { font.width(it.description) * 2 } ?: 0) + (iconWidth + conditionOffset)
        )

        if (node.children.isEmpty()) {
            return selfWidth
        }

        // 计算子节点们的最大宽度（递归计算每个子节点的宽度）
        val maxChildWidth = node.children.maxOfOrNull { calculateTreeWidth(it) } ?: 0

        // 总宽度取：当前节点自身宽度 和 (子节点数 * 同级最大子节点宽度) 的较大值
        return max(
            selfWidth,
            node.children.size * maxChildWidth
        )
    }

    private fun calculateTreeHeight(node: SkillTreeNode, currentY: Int): Int {
        val y = node.conditions.sumOf { font.lineHeight }
        val newY = currentY + y + verticalDrop
        return if (node.children.isEmpty()) newY
        else node.children.maxOf { calculateTreeHeight(it, newY + verticalDrop) }
    }

    private fun positionNodes(
        map: LinkedHashMap<SkillTreeNode, Pair<Int, Int>>,
        node: SkillTreeNode,
        initial: Pair<Int, Int>
    ) {
        map[node] = initial
        if (node.children.isEmpty()) return

        // 获取所有子节点的实际宽度（已考虑同级最大宽度）
        val childWidths = node.children.map { calculateTreeWidth(it) }
        val maxChildWidth = childWidths.maxOrNull() ?: 0

        // 计算起始X坐标（保证子节点居中）
        var childX = initial.first - (maxChildWidth * node.children.size) / 2

        node.children.forEach { child ->
            positionNodes(
                map,
                child,
                childX + maxChildWidth / 2 to initial.second + verticalDrop * 2 + child.conditions.sumOf { font.lineHeight }
            )
            childX += maxChildWidth // 使用统一的最大宽度进行布局
        }
    }

}