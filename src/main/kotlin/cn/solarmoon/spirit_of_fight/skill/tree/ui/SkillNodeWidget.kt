package cn.solarmoon.spirit_of_fight.skill.tree.ui

import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.CommonComponents

class SkillNodeWidget(
    val layout: SkillTreeNodeLayout,
    size: Int
): AbstractWidget(0, 0, size, size, CommonComponents.EMPTY) {

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        layout.render(guiGraphics)
        if (isHovered) {

        }
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}

}