package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class SOFLanguageProvider(
    output: PackOutput
): LanguageProvider(output, SpiritOfFight.MOD_ID, "zh_cn") {


    override fun addTranslations() {
        add("skill_tree_condition.also", " §4并§r ")
        add("skill_tree_condition.any", " §7或§r ")
        add("skill_tree_condition.comma", "§7，§r")
        add("skill_tree_condition.spirit_of_fight.any", "§7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.fight_spirit", "战意值 >= %s")
        add("skill_tree_condition.spirit_of_fight.wield_style", "%s")
        add("skill_tree_condition.spirit_of_fight.hit_target", "击中任意生物")
        add("skill_tree_condition.spirit_of_fight.sprinting", "正在奔跑")
        add("skill_tree_condition.spirit_of_fight.jumping", "正在跳跃")
        add("skill_tree_condition.spirit_of_fight.reverse", "§4不需要 §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.on_ground", "站在地面")
        add("skill_tree_condition.spirit_of_fight.skill_ended", "技能结束")
        add("skill_tree_condition.spirit_of_fight.off_hand", "副手持有: §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.press", "按住 §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.press_once", "按一次 §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.pulse", "按下 §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.release", "释放 §7[%s§7]")

        add("gui.spirit_of_fight.skill_tree.info", "缩放: %sx 拖动: 按住鼠标左键")

        add("wield_style.default", "默认架势")
        add("wield_style.special", "特殊架势")
    }


}