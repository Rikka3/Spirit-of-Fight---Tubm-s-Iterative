package cn.solarmoon.spirit_of_fight.skill.tree.ui

import cn.solarmoon.spark_core.util.ColorUtil
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import cn.solarmoon.spirit_of_fight.util.drawThickHLine
import cn.solarmoon.spirit_of_fight.util.drawThickVLine
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import java.awt.Color
import kotlin.math.max

class SkillTreeLayout(
    val tree: SkillTree,
    private val font: Font,
    private val fontSize: Double,
    private val iconSize: Int,
    private val verticalDrop: Int
) {

    val nodeLayouts = mutableMapOf<SkillTreeNode, SkillTreeNodeLayout>()
    private val interval = 20 // 节点间水平间距
    private val connectionThickness = 6
    private val offConnectionThickness = connectionThickness / 3
    private val titleBorderThickness = 2
    private val titleFontSize = fontSize * 2
    val titleWidth get() = (((Minecraft.getInstance().level?.let { font.width(tree.getName(it.registryAccess())) } ?: -4) + 4) * titleFontSize + titleBorderThickness * 2).toInt()
    val titleHeight = (font.lineHeight * 3 * titleFontSize + titleBorderThickness * 2).toInt()

    // 总尺寸
    var totalWidth = 0
        private set
    var totalHeight = 0
        private set

    var pos = 0 to 0
        set(value) {
            var x = if (tree.rootNodes.size > 1) value.first - totalWidth / 4 else value.first
            val a = tree.rootNodes.maxOf { calculateWidth(it) }
            tree.rootNodes.forEach { rootNode ->
                positionNodes(rootNode, x to value.second)
                x += a
            }
            field = value
        }

    init {
        tree.rootNodes.forEach { rootNode ->
            createNodeLayouts(rootNode)
        }

        totalWidth = tree.rootNodes.size * tree.rootNodes.maxOf { calculateWidth(it) } + (tree.rootNodes.size - 1) * interval
        totalHeight = tree.rootNodes.maxOf { calculateHeight(it, pos.second) } + titleHeight + verticalDrop * 2 // 标题加连线高度

        pos = 0 to 0
    }

    private fun createNodeLayouts(node: SkillTreeNode) {
        val layout = SkillTreeNodeLayout(node, font, fontSize, iconSize)
        nodeLayouts[node] = layout
        node.children.forEach { createNodeLayouts(it) }
    }

    private fun calculateWidth(node: SkillTreeNode): Int {
        val layout = nodeLayouts[node]!!
        val w = if (node in tree.rootNodes) max(layout.width, titleWidth) else layout.width

        if (node.children.isEmpty()) return w

        val maxWidth = node.children.maxOf { calculateWidth(it) } * node.children.size + interval * (node.children.size - 1)

        return max(w, maxWidth)
    }

    private fun calculateHeight(node: SkillTreeNode, currentY: Int): Int {
        val layout = nodeLayouts[node]!!
        val height = currentY + layout.height
        return (if (node.children.isEmpty()) height
        else node.children.maxOf { calculateHeight(it, height + verticalDrop * 2) })
    }

    private fun positionNodes(node: SkillTreeNode, initial: Pair<Int, Int>) {
        val initial = initial.first to if (node in tree.rootNodes) initial.second + nodeLayouts[node]!!.conditionHeight else initial.second
        nodeLayouts[node]!!.pos = initial
        if (node.children.isEmpty()) return

        val maxWidth = node.children.maxOf { calculateWidth(it) }
        val childTotalWidth = node.children.size * maxWidth

        // 计算起始X坐标（保证子节点居中）
        var childX = initial.first - childTotalWidth / 2 - interval * (node.children.size - 1) / 2

        node.children.forEach { child ->
            val childLayout = nodeLayouts[child]!!
            positionNodes(
                child,
                childX + maxWidth / 2 to initial.second + childLayout.lowerHeight + verticalDrop * 2 + childLayout.upperHeight
            )
            childX += maxWidth + interval
        }
    }

    private fun renderConnections(guiGraphics: GuiGraphics, node: SkillTreeNode) {
        val layout = nodeLayouts[node]!!
        val pose = guiGraphics.pose()
        pose.pushPose()
        pose.translate(0f, 0f, -1f)

        if (node.children.isEmpty()) {
            pose.popPose()
            return
        }

        // 主垂线
        pose.pushPose()
        pose.translate(0f, 0f, 1f)
        guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second + layout.lowerHeight + verticalDrop - 1, connectionThickness, Color.darkGray.rgb)
        guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second + layout.lowerHeight + verticalDrop - 1, offConnectionThickness, Color.white.rgb)
        pose.popPose()

        // 横线
        pose.pushPose()
        if (node.children.size > 1) {
            val start = nodeLayouts[node.children.first()]!!.pos.first - 1
            val end = nodeLayouts[node.children.last()]!!.pos.first + 1
            guiGraphics.drawThickHLine(start, end, layout.pos.second + layout.lowerHeight + verticalDrop, connectionThickness, Color.darkGray.rgb)
            guiGraphics.drawThickHLine(start, end, layout.pos.second + layout.lowerHeight + verticalDrop, offConnectionThickness, Color.white.rgb)
        }
        pose.popPose()

        // 各子分支线
        node.children.forEach {
            pose.pushPose()
            pose.translate(0f, 0f, -1f)
            val layout = nodeLayouts[it]!!
            guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second - layout.upperHeight - verticalDrop - connectionThickness / 2, connectionThickness, Color.darkGray.rgb)
            guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second - layout.upperHeight - verticalDrop - connectionThickness / 2, offConnectionThickness, Color.white.rgb)
            pose.popPose()
            // 覆盖白线以在拐弯处正确渲染
            guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second - layout.upperHeight - verticalDrop, offConnectionThickness, Color.white.rgb)
            renderConnections(guiGraphics, it)
        }
        pose.popPose()
    }

    fun render(guiGraphics: GuiGraphics) {
        val level = Minecraft.getInstance().level ?: return
        val pose = guiGraphics.pose()
        pose.pushPose()

//        guiGraphics.drawThickHLine(pos.first, pos.first + totalWidth / 2, pos.second, 6, Color.RED.rgb)
////        val u = titleHeight + verticalDrop * 2 + iconSize / 2
////        guiGraphics.fill(pos.first - totalWidth / 2, pos.second - u, pos.first + totalWidth / 2, pos.second - u + totalHeight, Color.darkGray.rgb)

//        if (tree.rootNodes.size > 1) {
//            pose.translate(-totalWidth / 4f, 0f, 0f)
//        }

        nodeLayouts.forEach { (node, layout) ->
            // 树标题
            pose.pushPose()
            if (node in tree.rootNodes) {
                val layout = nodeLayouts[node]!!

                if (node == tree.rootNodes.first()) {
                    val start = nodeLayouts[tree.rootNodes.first()]!!.pos
                    val end = nodeLayouts[tree.rootNodes.last()]!!.pos
                    val center = (start.first + end.first) / 2 to start.second - layout.upperHeight - verticalDrop * 2 - titleHeight / 2
                    guiGraphics.fill(center.first - titleWidth / 2, center.second - titleHeight / 2, center.first + titleWidth / 2, center.second + titleHeight / 2, Color.darkGray.rgb)
                    guiGraphics.fill(center.first - titleWidth / 2 + titleBorderThickness, center.second - titleHeight / 2 + titleBorderThickness, center.first + titleWidth / 2 - titleBorderThickness, center.second + titleHeight / 2 - titleBorderThickness, ColorUtil.getColorAndSetAlpha(0x36292f, 0.75f))
                    // 标题名
                    pose.pushPose()
                    val pos = (center.first / titleFontSize).toInt() to (center.second / titleFontSize - font.lineHeight * titleFontSize / 4).toInt()
                    pose.scale(titleFontSize.toFloat(), titleFontSize.toFloat(), 0f)
                    guiGraphics.drawCenteredString(font, tree.getName(level.registryAccess()), pos.first, pos.second, Color.white.rgb)
                    pose.popPose()
                    pose.pushPose()
                    pose.translate(0f, 0f, -1f)
                    // 标题横线
                    guiGraphics.drawThickHLine(start.first - 1, end.first + 1, center.second + titleHeight / 2 + verticalDrop, connectionThickness, Color.darkGray.rgb)
                    guiGraphics.drawThickHLine(start.first - 1, end.first + 1, center.second + titleHeight / 2 + verticalDrop, offConnectionThickness, Color.white.rgb)
                    // 标题向下竖线
                    guiGraphics.drawThickVLine(center.first, center.second, center.second + titleHeight / 2 + verticalDrop - 1, connectionThickness, Color.darkGray.rgb)
                    guiGraphics.drawThickVLine(center.first, center.second, center.second + titleHeight / 2 + verticalDrop - 1, offConnectionThickness, Color.white.rgb)
                    pose.popPose()
                }

                pose.pushPose()
                pose.translate(0f, 0f, -2f)
                // 根节点向上竖线
                guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second - layout.upperHeight - verticalDrop - connectionThickness / 2, connectionThickness, Color.darkGray.rgb)
                guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second - layout.upperHeight - verticalDrop - connectionThickness / 2, offConnectionThickness, Color.white.rgb)
                pose.popPose()
                guiGraphics.drawThickVLine(layout.pos.first, layout.pos.second, layout.pos.second - layout.upperHeight - verticalDrop, offConnectionThickness, Color.white.rgb)
            }

            // 连线
            renderConnections(guiGraphics, node)

            // 节点
            layout.render(guiGraphics)
            pose.popPose()
        }
        pose.popPose()
    }

}