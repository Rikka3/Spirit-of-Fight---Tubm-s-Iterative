package cn.solarmoon.spirit_of_fight.spirit

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ClientTickEvent

object FightSpiritGui: LayeredDraw.Layer {

    val EMPTY = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/gui/fight_spirit_empty.png")
    val FULL = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/gui/fight_spirit_full.png")

    var valueCache = 0

    @SubscribeEvent
    private fun tick(event: ClientTickEvent.Pre) {
        val player = Minecraft.getInstance().player ?: return
        val fs = player.getFightSpirit()
        valueCache = fs.value
    }

    override fun render(
        guiGraphics: GuiGraphics,
        deltaTracker: DeltaTracker
    ) {
        if (Minecraft.getInstance().options.hideGui) return
        val partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true)
        val poseStack = guiGraphics.pose()
        val player = Minecraft.getInstance().player ?: return
        val fs = player.getFightSpirit()

        val x = guiGraphics.guiWidth() / 2 - 8
        val y = guiGraphics.guiHeight() - 48
        val progress = fs.getProgress(partialTicks)
        val turn = if (fs.isFull) player.tickCount % 20 / 4 else 0 // 从0-4反复循环的数
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        poseStack.pushPose()
        guiGraphics.blit(FULL, x, y, 0f, turn.toFloat() * 10, 16, 10, 16, 50)
        poseStack.translate(0f, -progress * 10, 0f)
        guiGraphics.blit(EMPTY, x, y, 0f, -progress * 10, 16, 10, 16, 20)
        poseStack.popPose()

        RenderSystem.disableBlend()
    }

    private fun FightSpirit.getProgress(partialTicks: Float = 1f): Float {
        return (Mth.lerp(partialTicks, valueCache.toFloat(), value.toFloat()) / maxValue).coerceIn(0f, 1f)
    }

}