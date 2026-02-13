package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ClientTickEvent

/**
 * GUI layer for rendering the block break progress bar
 */
object BlockBreakBarGui : LayeredDraw.Layer {

    // Texture locations (reusing fight spirit bar for consistency)
    private val EMPTY = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/gui/fight_spirit_empty.png")
    private val FULL = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/gui/fight_spirit_full.png")

    override fun render(
        guiGraphics: GuiGraphics,
        deltaTracker: DeltaTracker
    ) {
        if (Minecraft.getInstance().options.hideGui) return
        val player = Minecraft.getInstance().player ?: return
        val blockBreakingData = player.getBlockBreakingData()

        // Only render if block is being broken and has progress
        if (!blockBreakingData.isBreaking || blockBreakingData.getProgress() <= 0.0f || blockBreakingData.getProgress() >= 1.0f) {
            return
        }

        val poseStack = guiGraphics.pose()
        val progress = blockBreakingData.getProgress()

        // Position above the fight spirit icon (centered)
        val x = guiGraphics.guiWidth() / 2 - 8
        val y = guiGraphics.guiHeight() - 58 // 10 pixels above fight spirit icon
        val barWidth = 16
        val barHeight = 4

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        poseStack.pushPose()

        // Draw background (empty bar)
        guiGraphics.blit(
            EMPTY,
            x, y,
            0f, 0f,
            barWidth, barHeight,
            16, 20
        )

        // Draw progress overlay (full bar)
        val progressWidth = (barWidth * progress).toInt()
        guiGraphics.blit(
            FULL,
            x, y,
            0f, 0f,
            progressWidth, barHeight,
            16, 50
        )

        poseStack.popPose()

        RenderSystem.disableBlend()
    }
}
