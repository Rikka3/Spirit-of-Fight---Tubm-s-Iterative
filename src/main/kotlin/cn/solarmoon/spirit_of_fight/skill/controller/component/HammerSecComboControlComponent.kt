package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerComboC1
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerComboC2
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer

class HammerSecComboControlComponent(
    private val startCombo: Skill<*>,
    comboC1: HammerComboC1,
    comboC2: HammerComboC2,
    endCombo: Skill<*>,
): ComboControlComponent(comboC1, comboC2, endCombo) {

    override val name: String = "hammer_combo"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
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