package cn.solarmoon.spirit_of_fight.skill.tree.ui

import cn.solarmoon.spark_core.util.ColorUtil
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.joml.Vector2f
import org.joml.Vector2i
import java.awt.Color
import kotlin.math.min
import kotlin.properties.Delegates

class SkillTreeScreen(
    private val itemStack: ItemStack,
    private val treeSet: SkillTreeSet
) : Screen(Component.literal("Skill Tree")) {

    lateinit var treeSetLayout: SkillTreeSetLayout

    val totalWidth get() = treeSetLayout.totalWidth
    val totalHeight get() = treeSetLayout.totalHeight

    // 头部参数
    private var itemSize = 32

    // 视图控制
    private var viewOffset = Vector2f()
    private var isDragging = false
    private var lastMousePos = Vector2f()
    private var currentScale = 1.0f
    private val minScale = 0.1f
    private val maxScale = 2.0f

    // 树布局参数
    private var verticalDrop = 64
    private val rootConditionMaxHeight get() = treeSetLayout.keys.maxOf { it.rootNode.conditions.maxOf { font.lineHeight } }

    private val partialTicks get() = Minecraft.getInstance().timer.getGameTimeDeltaPartialTick(true)

    override fun init() {
        super.init()
        treeSetLayout = SkillTreeSetLayout(treeSet, font, verticalDrop)
        autoCenterView()
    }

    private fun autoCenterView() {
        val screenWidth = width.toFloat()
        val screenHeight = height.toFloat()

        // 计算需要的缩放比例
        val widthRatio = screenWidth / (totalWidth * 1.2f)
        val heightRatio = screenHeight / (totalHeight * 1.2f)
        currentScale = min(widthRatio, heightRatio).coerceIn(minScale, maxScale)

        // 重置视图偏移
        viewOffset = Vector2f(-totalWidth * currentScale / 2 + treeSetLayout.firstEntry().value.totalWidth * currentScale / 4, -totalHeight * currentScale / 2)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        val pose = guiGraphics.pose()
        pose.pushPose()
        applyViewTransform(pose)

        renderHead(guiGraphics, mouseX, mouseY)
        renderConnections(guiGraphics) // 先画连线
        renderNodes(guiGraphics, mouseX, mouseY) // 再画节点和文字

        pose.popPose()
        renderHUD(guiGraphics)
    }

    private fun applyViewTransform(pose: PoseStack) {
        pose.translate(
            (width/2 + viewOffset.x).toDouble(),
            (height/2 + viewOffset.y).toDouble(),
            0.0
        )
        pose.scale(currentScale, currentScale, 1f)
    }

    private fun renderHead(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        val poseStack = guiGraphics.pose()
        poseStack.pushPose()
        val scale = itemSize.toFloat() / 16
        poseStack.scale(scale, scale, scale)
        val headCenter = Vector2i(treeSetLayout.totalWidth / 2 - itemSize / 2 - (treeSetLayout.firstEntry().value.totalWidth / 4).toInt(), - itemSize * 3 / 2 - verticalDrop * 2 - rootConditionMaxHeight)
        guiGraphics.renderItem(itemStack, (headCenter.x / scale).toInt(), (headCenter.y / scale).toInt())
        poseStack.popPose()
    }

    private fun renderNodes(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        treeSetLayout.forEach { (tree, layout) ->
//            val pos = layout.pos
//            val x = pos.x - layout.totalWidth / 2
//            val y = pos.y - layout.iconHeight / 2
//            guiGraphics.fill(
//                x, y,
//                x + layout.totalWidth,
//                y + layout.totalHeight,
//                0x80000000.toInt()
//            )
            layout.nodePositions.forEach { (node, pos) ->
                val screenX = pos.first
                val screenY = pos.second

                // 绘制节点图标
                guiGraphics.blit(
                    node.icon,
                    screenX - layout.iconWidth / 2,
                    screenY - layout.iconHeight / 2,
                    0f, 0f,
                    layout.iconWidth, layout.iconHeight,
                    layout.iconWidth, layout.iconHeight,
                )

                val namePosX = screenX - font.width(node.name) / 2
                val namePosY = screenY + layout.iconHeight / 2 + 8

                // 技能名背景
                guiGraphics.fill(
                    namePosX - 2, namePosY - 2,
                    namePosX + font.width(node.name) + 2, namePosY + font.lineHeight + 2,
                    ColorUtil.getColorAndSetAlpha(Color.DARK_GRAY.rgb, 0.75f)
                )

                // 绘制技能名称
                guiGraphics.drawString(
                    font,
                    node.name,
                    namePosX,
                    namePosY,
                    0xFFFFFF,
                    true
                )

                // 检测悬停
                if (isHovering(screenX, screenY, mouseX, mouseY)) {
                    renderTooltip(guiGraphics, node, mouseX, mouseY)
                }
            }
        }
    }

    private fun renderConnections(guiGraphics: GuiGraphics) {
        treeSetLayout.forEach { (tree, layout) ->
            layout.nodePositions.forEach { (node, parentPos) ->
                if (node == tree.rootNode) {
                    val headCenter = Vector2i(treeSetLayout.totalWidth / 2 - (treeSetLayout.firstEntry().value.totalWidth / 4).toInt(), -verticalDrop * 2 - rootConditionMaxHeight - layout.iconHeight / 2)
                    drawConnection(
                        guiGraphics,
                        headCenter.x to headCenter.y,
                        parentPos.first to parentPos.second,
                        layout,
                        node.conditions
                    )
                }
                node.children.forEach { child ->
                    val childPos = layout.nodePositions[child]!!
                    drawConnection(
                        guiGraphics,
                        parentPos.first to parentPos.second,
                        childPos.first to childPos.second,
                        layout,
                        node.conditions
                    )
                }
            }
        }
    }

    private fun drawConnection(
        guiGraphics: GuiGraphics,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        layout: SkillTreeLayout,
        conditions: List<SkillTreeCondition>
    ) {
        val (startX, startY) = start
        val (endX, endY) = end

        // 分三段绘制连接线
        val thickLineWidth = 6 // 粗线宽度（像素）
        val overlapT = thickLineWidth / 2
        val overlap = thickLineWidth / 3 / 2
        val branchEndY = endY // 分支垂直线终点

        val baseLineColor = ColorUtil.getColorAndSetAlpha(Color.BLACK.rgb, 1f) // 基底线颜色（深灰色）
        val mainLineColor = ColorUtil.getColorAndSetAlpha(Color.WHITE.rgb, 1f) // 主细线颜色

        // 绘制基底粗线
        val poseStack = guiGraphics.pose()
        poseStack.pushPose()
        poseStack.translate(0f, 0f, -1000f)
        // 1. 主垂直线粗线
        drawThickVLine(guiGraphics, startX, startY - overlapT, startY + verticalDrop, thickLineWidth, baseLineColor)

        // 2. 水平连接粗线
        drawThickHLine(guiGraphics, startX, endX, startY + verticalDrop, thickLineWidth, baseLineColor)

        // 3. 分支垂直线粗线
        drawThickVLine(guiGraphics, endX, startY - overlapT + verticalDrop, endY, thickLineWidth, baseLineColor)
        poseStack.popPose()

        // 绘制主细线
        // 1. 主垂直线粗线
        drawThickVLine(guiGraphics, startX, startY - overlap, startY + verticalDrop, thickLineWidth / 3, mainLineColor)

        // 2. 水平连接粗线
        drawThickHLine(guiGraphics, startX, endX, startY + verticalDrop, thickLineWidth / 3, mainLineColor)

        // 3. 分支垂直线粗线
        drawThickVLine(guiGraphics, endX, startY - overlap + verticalDrop, endY, thickLineWidth / 3, mainLineColor)

        // 条件文本的起始Y坐标
        val baseTextY = (startY + verticalDrop + branchEndY) / 2 - conditions.sumOf { font.lineHeight }
        var textOffsetY = 0
        conditions.forEach { condition ->
            val text = condition.description
            val textHeight = font.lineHeight

            val textX = endX + layout.conditionOffset
            val textY = baseTextY + textOffsetY

            // 绘制文字
            guiGraphics.drawString(
                font,
                text.string,
                textX,
                textY,
                0xFFFF00,
                false
            )

            // 按实际行高累加偏移量
            textOffsetY += textHeight // 4像素的行间距
        }
    }

    private fun drawThickHLine(
        guiGraphics: GuiGraphics,
        xStart: Int,
        xEnd: Int,
        y: Int,
        thickness: Int,
        color: Int
    ) {
        val halfThickness = thickness / 2f
        val poseStack = guiGraphics.pose()
        poseStack.pushPose()
        poseStack.translate(0f, -halfThickness, 0f)
        guiGraphics.fill(
            xStart, y,
            xEnd, y + thickness,
            color
        )
        poseStack.popPose()
    }

    private fun drawThickVLine(
        guiGraphics: GuiGraphics,
        x: Int,
        yStart: Int,
        yEnd: Int,
        thickness: Int,
        color: Int
    ) {
        val halfThickness = thickness / 2f
        val poseStack = guiGraphics.pose()
        poseStack.pushPose()
        poseStack.translate(-halfThickness, 0f, 0f)
        guiGraphics.fill(
            x, yStart,
            x + thickness, yEnd,
            color
        )
        poseStack.popPose()
    }

    private fun isHovering(nodeX: Int, nodeY: Int, mouseX: Int, mouseY: Int): Boolean {
        val transformedX = (mouseX - width/2 - viewOffset.x) / currentScale
        val transformedY = (mouseY - height/2 - viewOffset.y) / currentScale
        val textureSize = treeSetLayout.firstEntry().value.iconWidth.toDouble() / 2
        return transformedX in (nodeX-textureSize..nodeX+textureSize) &&
                transformedY in (nodeY-textureSize..nodeY+textureSize)
    }

    private fun renderTooltip(guiGraphics: GuiGraphics, node: SkillTreeNode, mouseX: Int, mouseY: Int) {
        val transformedX = (mouseX - width/2 - viewOffset.x) / currentScale
        val transformedY = (mouseY - height/2 - viewOffset.y) / currentScale

        guiGraphics.renderTooltip(
            font,
            node.description,
            transformedX.toInt(),
            transformedY.toInt()
        )
    }

    private fun renderHUD(guiGraphics: GuiGraphics) {
        val infoText = "缩放: ${"%.01f".format(currentScale)}x  拖动: 按住鼠标中键"
        guiGraphics.drawString(
            font,
            infoText,
            10,
            height - 20,
            0xAAAAAA,
            true
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0) { // 中键拖动
            isDragging = true
            lastMousePos = Vector2f(mouseX.toFloat(), mouseY.toFloat())
            return true
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0) {
            isDragging = false
            return true
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        if (isDragging) {
            viewOffset.add(
                (mouseX - lastMousePos.x).toFloat(),
                (mouseY - lastMousePos.y).toFloat()
            )
            lastMousePos = Vector2f(mouseX.toFloat(), mouseY.toFloat())
            return true
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        val zoomFactor = if (scrollY > 0) 1.1f else 0.9f
        val oldScale = currentScale
        val newScale = (currentScale * zoomFactor).coerceIn(minScale, maxScale)

        // 计算鼠标在场景空间中的位置
        val mouseSceneX = (mouseX - width/2 - viewOffset.x) / oldScale
        val mouseSceneY = (mouseY - height/2 - viewOffset.y) / oldScale

        // 调整偏移量保持鼠标位置不变
        viewOffset.x = (mouseX - width/2).toFloat() - mouseSceneX.toFloat() * newScale
        viewOffset.y = (mouseY - height/2).toFloat() - mouseSceneY.toFloat() * newScale

        currentScale = newScale
        return true
    }

}