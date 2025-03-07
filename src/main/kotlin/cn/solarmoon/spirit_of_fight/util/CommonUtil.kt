package cn.solarmoon.spirit_of_fight.util

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.item.crafting.Ingredient

fun Ingredient.getName(): String {
    // 遍历所有可能的成分类型
    values.forEach { value ->
        // 如果是标签类型
        if (value is Ingredient.TagValue) {
            return "#${value.tag.location()}" // 例如 #minecraft:swords
        }
        // 如果是物品类型（单物品）
        if (value is Ingredient.ItemValue) {
            val item = value.item
            item.let { return it.displayName.toString() } // 例如 minecraft:stone
        }
    }
    // 默认返回空或自定义文本
    return "unknown"
}

fun Font.width(s: FormattedText, size: Double) = width(s) * size

fun Font.lineHeight(size: Double) = lineHeight * size

fun GuiGraphics.drawThickHLine(
    xStart: Int,
    xEnd: Int,
    y: Int,
    thickness: Int,
    color: Int
) {
    val halfThickness = thickness / 2f
    val poseStack = pose()
    poseStack.pushPose()
    poseStack.translate(0f, -halfThickness, 0f)
    fill(
        xStart, y,
        xEnd, y + thickness,
        color
    )
    poseStack.popPose()
}

fun GuiGraphics.drawThickVLine(
    x: Int,
    yStart: Int,
    yEnd: Int,
    thickness: Int,
    color: Int
) {
    val halfThickness = thickness / 2f
    val poseStack = pose()
    poseStack.pushPose()
    poseStack.translate(-halfThickness, 0f, 0f)
    fill(
        x, yStart,
        x + thickness, yEnd,
        color
    )
    poseStack.popPose()
}