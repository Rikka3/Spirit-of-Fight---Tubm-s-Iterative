package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class SOFLanguageProvider(
    output: PackOutput
): LanguageProvider(output, SpiritOfFight.MOD_ID, "zh_cn") {


    override fun addTranslations() {
        add("skill.spirit_of_fight.hammer_combo_0.name", "战锤:连招1")
        add("skill.spirit_of_fight.hammer_combo_1.name", "战锤:连招2")
        add("skill.spirit_of_fight.hammer_combo_2.name", "战锤:连招3")
        add("skill.spirit_of_fight.hammer_combo_c0.name", "战锤:铁山靠")
        add("skill.spirit_of_fight.hammer_combo_02.name", "战锤:变招2")
        add("skill.spirit_of_fight.hammer_sprinting_attack.name", "战锤:大旋风")
        add("skill_tree_condition.spirit_of_fight.key_input", "需要按键：[%s]")
    }


}