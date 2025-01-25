package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer

class ChangeableComboControlComponent(
    vararg comboList: Skill<*>,
    val switchComboIndex: Int = 0,
    val inputWindow: IntRange = 1..4
): ComboControlComponent(*comboList) {

    override val name: String = "changeableCombo"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        // 对可变招技能管理器的预输入控制
        if (comboList[switchComboIndex].runTime in inputWindow) preInput.executeIfPresent("combo")
        return super.localControl(controller, player, preInput, input)
    }

}