package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC1
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC2
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.common.Tags

class ShieldComboControlComponent(
    private val startCombo: Skill<*>,
    comboC1: ShieldComboC1,
    comboC2: ShieldComboC2,
    endCombo: Skill<*>,
    private val isHoldingShield: LivingEntity.() -> Boolean = { offhandItem.`is`(Tags.Items.TOOLS_SHIELD) }
): ComboControlComponent(comboC1, comboC2, endCombo) {

    override val name: String = "shield_combo"

    fun isHoldingShield(entity: LivingEntity) = isHoldingShield.invoke(entity)

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (!isHoldingShield(player)) return false

        val start = controller.onPressOnce(SOFKeyMappings.SPECIAL_ATTACK) {
            if (!startCombo.isActive()) return@onPressOnce false

            preInput.setInput("combo", 5) {
                index.increment()
                combo.activate()
                sendPackage(player.id, index.get())
            }
            true
        }

        if (start) return true
        if (index.get() == maxComboAmount - 1 ) return false

        return super.localControl(controller, player, preInput, input)
    }

}