package cn.solarmoon.spirit_of_fight.skill.tree.ui

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import cn.solarmoon.spirit_of_fight.util.lineHeight
import cn.solarmoon.spirit_of_fight.util.width
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.FormattedText
import java.awt.Color

class SkillTreeNodeLayout(
    val node: SkillTreeNode,
    val font: Font,
    var fontSize: Double,
    var iconSize: Int
) {

    private val fontOffset = 4
    private val conditionColor = ChatFormatting.YELLOW.color!!
    private val nameColor = Color.WHITE.rgb
    private val nameBackgroundColor = Color.DARK_GRAY.rgb
    private val nameBorderThickness = 2

    var pos = 0 to 0 // 坐标实际上是相当于一个(width, height)的长方形的左上角，但通过渲染坐标将位于Icon中心（并非整个长方形中心，而是图像中心）

    val conditionHeight = fontOffset + node.conditions.size * fontLineHeight() + (node.conditions.size - 1) // 条件占的总高度（用于计算连线根据条件长度延申的拓展长度）

    val nameHeight = if (node.name == null) 0 else fontOffset * 2 + fontLineHeight() // 名子行高 + 名字下移量 * 2 （上下对称）

    val upperHeight = conditionHeight + iconSize / 2

    val lowerHeight = iconSize / 2 + nameHeight

    val center get() = pos.first to pos.second + iconSize / 2 + nameHeight - height / 2 // 真正的中心，通过图像中心加上 半个图像长 + 名字总高度 获取底边y值再减去一半高度

    val width: Int get() {
        // 当前节点自身需要的宽度（名字与描述间的最大宽度）
        // 描述固定在右侧，故描述宽度为 描述长度 * 2（对称） + 图标大小 + 描述与中线的偏移量 * 2（对称）
        return maxOf(
            (node.name?.let { fontWidth(it) } ?: 0) + nameBorderThickness * 2,
            (node.conditions.maxOfOrNull { fontWidth(it.description) * 2 } ?: 0) + (fontOffset * 2)
        )
    }

    val height: Int get() {
        // 单个节点不考虑其它任何情况下，其总高度应为：图像高度 + (所有条件行高（每个条件占一行） + 条件相对于图像往上的偏移量（为了好看）) + (名字行高 + 名字往下的偏移)
        return conditionHeight + iconSize + nameHeight
    }

    fun render(guiGraphics: GuiGraphics) {
        val poseStack = guiGraphics.pose()
        poseStack.pushPose()

//        guiGraphics.fill(
//            center.first - width / 2, center.second - height / 2,
//            center.first + width / 2, center.second + height / 2,
//            Color.orange.rgb
//        )

        // 条件描述
        poseStack.pushPose()
        poseStack.scale(fontSize.toFloat(), fontSize.toFloat(), 0f)
        node.conditions.forEachIndexed { index, condition ->
            // 计算原始坐标
            val rawX = pos.first + fontOffset
            val rawY = pos.second - iconSize/2 - fontOffset - fontLineHeight()*(index+1)

            // 将坐标反向缩放（抵消scale影响）
            val scaledX = rawX / fontSize
            val scaledY = rawY / fontSize

            guiGraphics.drawString(
                font,
                condition.description,
                scaledX.toInt(),
                scaledY.toInt(),
                conditionColor
            )
        }
        poseStack.popPose()

        // 图标
        guiGraphics.blit(node.icon, pos.first - iconSize / 2, pos.second - iconSize / 2, 0f, 0f, iconSize, iconSize, iconSize, iconSize)

        node.name?.let { name ->
            // 名字透明背景
            guiGraphics.fill(
                pos.first - fontWidth(name) / 2 - nameBorderThickness, pos.second + iconSize / 2 + fontOffset - nameBorderThickness,
                pos.first + fontWidth(name) / 2 + nameBorderThickness, pos.second + iconSize / 2 + fontOffset + fontLineHeight() + nameBorderThickness,
                nameBackgroundColor
            )

            // 名字
            poseStack.pushPose()
            poseStack.scale(fontSize.toFloat(), fontSize.toFloat(), 0f)
            // 计算居中坐标并反向缩放
            val nameX = (pos.first) / fontSize
            val nameY = (pos.second + iconSize/2 + fontOffset) / fontSize
            guiGraphics.drawCenteredString(
                font,
                name,
                nameX.toInt(),
                nameY.toInt(),
                nameColor
            )
            poseStack.popPose()
        }

        // Cooldown Overlay
        val player = Minecraft.getInstance().player
        if (player != null && node is CommonNode && node.cooldown > 0 && !player.isCreative) {
            val patch = (player as Any) as? IEntityPatch
            if (patch != null) {
                val lastUsed = patch.skillCooldowns[node.skillLocation] ?: 0L
                val elapsed = player.level().gameTime - lastUsed
                if (elapsed < node.cooldown) {
                    val remaining = node.cooldown - elapsed
                    val progress = remaining.toFloat() / node.cooldown

                    // Render gray overlay (from bottom up)
                    guiGraphics.fill(
                        pos.first - iconSize / 2,
                        (pos.second + iconSize / 2 - iconSize * progress).toInt(),
                        pos.first + iconSize / 2,
                        pos.second + iconSize / 2,
                        0x77000000.toInt()
                    )

                    // Render remaining time text
                    val remainingSeconds = remaining / 20.0
                    val text = if (remainingSeconds >= 1.0) "%.1fs".format(remainingSeconds) else "%.1fs".format(remainingSeconds)
                    poseStack.pushPose()
                    poseStack.scale(fontSize.toFloat() * 0.8f, fontSize.toFloat() * 0.8f, 1f)
                    val textX = (pos.first / (fontSize * 0.8f)).toInt()
                    val textY = (pos.second / (fontSize * 0.8f) - font.lineHeight / 2).toInt()
                    guiGraphics.drawCenteredString(font, text, textX, textY, 0xFFFFFF)
                    poseStack.popPose()
                }
            }
        }

        poseStack.popPose()
    }

    private fun fontWidth(s: FormattedText) = font.width(s, fontSize).toInt()
    private fun fontLineHeight() = font.lineHeight(fontSize).toInt()

}