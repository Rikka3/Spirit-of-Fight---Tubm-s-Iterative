package cn.solarmoon.spirit_of_fight.skill.tree.ui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.CommonComponents
import org.joml.Vector2f

class ViewPortController(
    private val screen: Screen
): AbstractWidget(0, 0, 0, 0, CommonComponents.EMPTY) {

    var currentScale = 1.0f
    var isDragging = false
    val viewOffset = Vector2f()
    val lastMousePos = Vector2f()
    val minScale = 0.1f
    val maxScale = 2.0f

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        width = screen.width
        height = screen.height
    }

    fun applyTransform(poseStack: PoseStack) {
        poseStack.translate(
            (width/2 + viewOffset.x).toDouble(),
            (height/2 + viewOffset.y).toDouble(),
            0.0
        )
        poseStack.scale(currentScale, currentScale, 1f)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0) {
            isDragging = true
            lastMousePos.set(mouseX.toFloat(), mouseY.toFloat())
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
            lastMousePos.set(mouseX.toFloat(), mouseY.toFloat())
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

    fun screenToScene(mouseX: Int, mouseY: Int): Vector2f {
        return Vector2f(
            (mouseX - width/2 - viewOffset.x) / currentScale,
            (mouseY - height/2 - viewOffset.y) / currentScale
        )
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}

    override fun isMouseOver(mouseX: Double, mouseY: Double) = true

    override fun playDownSound(handler: SoundManager) {}

}