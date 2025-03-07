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
        add("skill.spirit_of_fight.hammer_combo_c0.description", "铁山靠以近身短打进行出其不意的攻击，击中对手时会立刻使其失衡（减速MAX）")
        add("skill.spirit_of_fight.hammer_combo_c1.name", "战锤:变招2")
        add("skill.spirit_of_fight.hammer_sprinting_attack.name", "战锤:大旋风")

        add("skill_tree_condition.also", " §4并§r ")
        add("skill_tree_condition.any", " §7或§r ")
        add("skill_tree_condition.comma", "§7，§r")
        add("skill_tree_condition.spirit_of_fight.any", "〘%s〙")
        add("skill_tree_condition.spirit_of_fight.fight_spirit", "战意值 >= %s")
        add("skill_tree_condition.spirit_of_fight.hit_target", "击中任意生物")
        add("skill_tree_condition.spirit_of_fight.sprinting", "正在奔跑")
        add("skill_tree_condition.spirit_of_fight.jumping", "正在跳跃")
        add("skill_tree_condition.spirit_of_fight.reverse", "§4不需要§r〘%s〙")
        add("skill_tree_condition.spirit_of_fight.on_ground", "站在地面")
        add("skill_tree_condition.spirit_of_fight.skill_ended", "技能结束")
        add("skill_tree_condition.spirit_of_fight.key_input.press", "按住 [%s]")
        add("skill_tree_condition.spirit_of_fight.key_input.press_once", "按下 [%s]")
        add("skill_tree_condition.spirit_of_fight.key_input.release", "释放 [%s]")

        add("gui.spirit_of_fight.skill_tree.info", "缩放: %sx 拖动：按住鼠标左键")
    }


}