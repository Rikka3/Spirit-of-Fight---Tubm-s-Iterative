package cn.solarmoon.spirit_of_fight.client

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import kotlin.math.max

/**
 * Renders small cooldown bars above the hotbar for combat abilities.
 * Shows cooldowns for:
 * - Switch Attack (2 seconds)
 * - Combo (1 second)
 * - Dodge (1 second)
 * - Jump Attack (1 second)
 */
@EventBusSubscriber(value = [Dist.CLIENT])
object CooldownBarRenderer {

    // Bar dimensions - small and minecrafty
    private const val BAR_WIDTH = 40
    private const val BAR_HEIGHT = 3
    private const val BAR_SPACING = 5
    private const val ICON_SIZE = 8

    // Colors for different cooldown types (ARGB format)
    private const val SWITCH_ATTACK_COLOR = 0xFF5555FF.toInt() // Blue
    private const val COMBO_COLOR = 0xFFFF5555.toInt() // Red
    private const val DODGE_COLOR = 0xFF55FF55.toInt() // Green
    private const val JUMP_ATTACK_COLOR = 0xFFFFFF55.toInt() // Yellow
    private const val SPRINT_ATTACK_COLOR = 0xFFFFAA55.toInt() // Orange
    private const val BACKGROUND_COLOR: Int = 0x80000000.toInt() // Semi-transparent black
    private const val BORDER_COLOR = 0xFF373737.toInt() // Dark gray border

    @SubscribeEvent
    @JvmStatic
    fun onRenderHotbar(event: RenderGuiLayerEvent.Post) {
        val minecraft = Minecraft.getInstance()
        val player = minecraft.player ?: return
        val options = minecraft.options

        // Only handle hotbar layer
        if (event.name != VanillaGuiLayers.HOTBAR) return

        // Don't render if player is spectator or GUI is hidden
        if (player.isSpectator) return
        if (options.hideGui) return

        // Only render in first person (third person has its own considerations)
        if (!options.cameraType.isFirstPerson) return

        val patch = (player as? Any) as? IEntityPatch ?: return
        val guiGraphics = event.guiGraphics
        val screenWidth = minecraft.window.guiScaledWidth
        val screenHeight = minecraft.window.guiScaledHeight
        val currentTime = player.level().gameTime

        // Calculate position - above the hotbar, centered
        val centerX = screenWidth / 2
        val startY = screenHeight - 50 // Above hotbar

        // Collect active cooldowns
        val cooldowns = mutableListOf<CooldownInfo>()

        // Switch Attack cooldown (2 seconds = 40 ticks)
        val switchCooldownEnd = patch.switchAttackCooldownUntil
        if (switchCooldownEnd > currentTime) {
            val remaining = switchCooldownEnd - currentTime
            val progress = 1.0f - (remaining.toFloat() / 40f)
            cooldowns.add(CooldownInfo("Switch", progress.coerceIn(0f, 1f), SWITCH_ATTACK_COLOR))
        }

        // Combo cooldown (1 second = 20 ticks)
        val comboCooldownEnd = patch.comboCooldownUntil
        if (comboCooldownEnd > currentTime) {
            val remaining = comboCooldownEnd - currentTime
            val progress = 1.0f - (remaining.toFloat() / 20f)
            cooldowns.add(CooldownInfo("Combo", progress.coerceIn(0f, 1f), COMBO_COLOR))
        }

        // Dodge cooldown (1 second = 20 ticks)
        val dodgeCooldownEnd = patch.lastDodgeTick + 20
        if (dodgeCooldownEnd > currentTime) {
            val remaining = dodgeCooldownEnd - currentTime
            val progress = 1.0f - (remaining.toFloat() / 20f)
            cooldowns.add(CooldownInfo("Dodge", progress.coerceIn(0f, 1f), DODGE_COLOR))
        }

        // Jump Attack cooldown (1 second = 20 ticks)
        val jumpAttackCooldownEnd = patch.lastJumpAttackTick + 20
        if (jumpAttackCooldownEnd > currentTime) {
            val remaining = jumpAttackCooldownEnd - currentTime
            val progress = 1.0f - (remaining.toFloat() / 20f)
            cooldowns.add(CooldownInfo("Jump", progress.coerceIn(0f, 1f), JUMP_ATTACK_COLOR))
        }

        // Sprint Attack cooldown (3 seconds = 60 ticks)
        val sprintAttackCooldownEnd = patch.sprintAttackCooldownUntil
        if (sprintAttackCooldownEnd > currentTime) {
            val remaining = sprintAttackCooldownEnd - currentTime
            val progress = 1.0f - (remaining.toFloat() / 60f)
            cooldowns.add(CooldownInfo("Sprint", progress.coerceIn(0f, 1f), SPRINT_ATTACK_COLOR))
        }

        // Render cooldown bars
        if (cooldowns.isNotEmpty()) {
            renderCooldownBars(guiGraphics, centerX, startY, cooldowns)
        }
    }

    private fun renderCooldownBars(guiGraphics: GuiGraphics, centerX: Int, startY: Int, cooldowns: List<CooldownInfo>) {
        val totalWidth = cooldowns.size * BAR_WIDTH + (cooldowns.size - 1) * BAR_SPACING
        var currentX = centerX - totalWidth / 2

        for (cooldown in cooldowns) {
            renderSingleBar(guiGraphics, currentX, startY, cooldown)
            currentX += BAR_WIDTH + BAR_SPACING
        }
    }

    private fun renderSingleBar(guiGraphics: GuiGraphics, x: Int, y: Int, cooldown: CooldownInfo) {
        // Draw background (empty bar)
        guiGraphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, BACKGROUND_COLOR)

        // Draw border
        guiGraphics.renderOutline(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, BORDER_COLOR)

        // Draw progress fill
        val fillWidth = (BAR_WIDTH * cooldown.progress).toInt()
        if (fillWidth > 0) {
            guiGraphics.fill(x, y, x + fillWidth, y + BAR_HEIGHT, cooldown.color)
        }

        // Draw label above bar (small text)
        val font = Minecraft.getInstance().font
        val labelWidth = font.width(cooldown.name)
        val labelX = x + (BAR_WIDTH - labelWidth) / 2
        val labelY = y - 8

        // Draw shadow for readability
        guiGraphics.drawString(font, cooldown.name, labelX + 1, labelY + 1, 0x00000000)
        guiGraphics.drawString(font, cooldown.name, labelX, labelY, 0xFFFFFFFF.toInt())
    }

    private data class CooldownInfo(
        val name: String,
        val progress: Float,
        val color: Int
    )
}
