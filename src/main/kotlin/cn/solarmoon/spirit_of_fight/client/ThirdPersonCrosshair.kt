package cn.solarmoon.spirit_of_fight.client

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

/**
 * Renders the vanilla crosshair in third person view.
 * Minecraft by default only shows crosshair in first person view.
 * This class ensures the crosshair is also visible in third person.
 */
@EventBusSubscriber(value = [Dist.CLIENT])
object ThirdPersonCrosshair {

    // The crosshair sprite location in Minecraft 1.21.1
    private val CROSSHAIR_SPRITE: ResourceLocation = ResourceLocation.withDefaultNamespace("hud/crosshair")

    @SubscribeEvent
    @JvmStatic
    fun onRenderCrosshair(event: RenderGuiLayerEvent.Post) {
        val minecraft = Minecraft.getInstance()
        val player = minecraft.player ?: return
        val options = minecraft.options

        // Only render in third person view (not first person)
        if (options.cameraType.isFirstPerson) return

        // Only handle crosshair layer
        if (event.name != VanillaGuiLayers.CROSSHAIR) return

        // Don't render if player is spectator or GUI is hidden
        if (player.isSpectator) return
        if (options.hideGui) return

        val guiGraphics = event.guiGraphics
        val screenWidth = minecraft.window.guiScaledWidth
        val screenHeight = minecraft.window.guiScaledHeight
        
        // Render crosshair using the same approach as vanilla 1.21.1
        val centerX = screenWidth / 2
        val centerY = screenHeight / 2
        
        // Set up blend mode exactly like vanilla does for crosshair
        // This makes the crosshair invert colors for visibility on any background
        RenderSystem.enableBlend()
        RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
            GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        )
        
        // Use blitSprite like vanilla does in 1.21.1
        guiGraphics.blitSprite(CROSSHAIR_SPRITE, centerX - 7, centerY - 7, 15, 15)
        
        RenderSystem.defaultBlendFunc()
    }
}
