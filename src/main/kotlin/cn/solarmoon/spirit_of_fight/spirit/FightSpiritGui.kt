package cn.solarmoon.spirit_of_fight.spirit

import cn.solarmoon.spark_core.util.blitTransparent
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import java.awt.Color

class FightSpiritGui: LayeredDraw.Layer {
    
    companion object {
        val EMPTY = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/gui/fight_spirit_empty.png")
        val FULL = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/gui/fight_spirit_full.png")
    }

    override fun render(
        guiGraphics: GuiGraphics,
        deltaTracker: DeltaTracker
    ) {
        val partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true)
        val player = Minecraft.getInstance().player ?: return
        val fs = player.getFightSpirit()

        val x = guiGraphics.guiWidth() / 2 - 8f
        val y = guiGraphics.guiHeight() - 48f
        val progress = fs.getProgress(partialTicks)
        val turn = player.tickCount % 20 / 4 // 从0-4反复循环的数
        guiGraphics.blitTransparent(EMPTY, x, y, 1f, 1f, 16f, 10f, Color.WHITE.rgb)
        guiGraphics.blitTransparent(FULL, x, y + 10f, 0f, 1f, 0f + turn/5f, -progress * 1f / 5 + turn/5f, 16f, -progress * 10f, Color.WHITE.rgb)
    }

}