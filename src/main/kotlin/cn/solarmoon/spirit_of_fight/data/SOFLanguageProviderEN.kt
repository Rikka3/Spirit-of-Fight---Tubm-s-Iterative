package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class SOFLanguageProviderEN(
    output: PackOutput
): LanguageProvider(output, SpiritOfFight.MOD_ID, "en_us") {

    override fun addTranslations() {
        // Item names
        add("item.spirit_of_fight.leather_gloves", "Leather Gloves")
        add("item.spirit_of_fight.iron_gloves", "Iron Gauntlets")
        add("item.spirit_of_fight.golden_gloves", "Golden Gauntlets")
        add("item.spirit_of_fight.diamond_gloves", "Diamond Gauntlets")
        add("item.spirit_of_fight.netherite_gloves", "Netherite Gauntlets")
        add("item.spirit_of_fight.wooden_warhammer", "Wooden Warhammer")
        add("item.spirit_of_fight.stone_warhammer", "Stone Warhammer")
        add("item.spirit_of_fight.iron_warhammer", "Iron Warhammer")
        add("item.spirit_of_fight.golden_warhammer", "Golden Warhammer")
        add("item.spirit_of_fight.diamond_warhammer", "Diamond Warhammer")
        add("item.spirit_of_fight.netherite_warhammer", "Netherite Warhammer")
        add("item.spirit_of_fight.wooden_spear", "Wooden Spear")
        add("item.spirit_of_fight.stone_spear", "Stone Spear")
        add("item.spirit_of_fight.iron_spear", "Iron Spear")
        add("item.spirit_of_fight.golden_spear", "Golden Spear")
        add("item.spirit_of_fight.diamond_spear", "Diamond Spear")
        add("item.spirit_of_fight.netherite_spear", "Netherite Spear")
        
        // Skill tree conditions
        add("skill_tree_condition.also", " §4and§r ")
        add("skill_tree_condition.any", " §7or§r ")
        add("skill_tree_condition.comma", "§7,§r")
        add("skill_tree_condition.spirit_of_fight.any", "§7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.fight_spirit", "Fight Spirit >= %s")
        add("skill_tree_condition.spirit_of_fight.wield_style", "%s")
        add("skill_tree_condition.spirit_of_fight.hit_target", "Hit any mob")
        add("skill_tree_condition.spirit_of_fight.sprinting", "Sprinting")
        add("skill_tree_condition.spirit_of_fight.jumping", "Jumping")
        add("skill_tree_condition.spirit_of_fight.reverse", "§4Not §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.on_ground", "Standing on ground")
        add("skill_tree_condition.spirit_of_fight.skill_ended", "Skill ended")
        add("skill_tree_condition.spirit_of_fight.input_direction", "Input direction: §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.off_hand", "Off-hand holding: §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.press", "Hold §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.press_once", "Press once §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.pulse", "Press §7[%s§7]")
        add("skill_tree_condition.spirit_of_fight.key_input.release", "Release §7[%s§7]")

        // GUI texts
        add("gui.spirit_of_fight.skill_tree.info", "Zoom: %sx Drag: Hold Left Mouse Button")

        // Wield styles
        add("wield_style.default", "Default Stance")
        add("wield_style.special", "Special Stance")
        
        // Creative tab
        add("creative_mode_tab.spirit_of_fight.main", "Spirit of Fight")
        
        // Key bindings
        add("key.spirit_of_fight.category", "Spirit of Fight")
        add("key.spirit_of_fight.block", "Block")
        add("key.spirit_of_fight.dodge", "Dodge")
        add("key.spirit_of_fight.switch_posture", "Switch Posture")
        add("key.spirit_of_fight.special_attack", "Special Attack")
        add("key.spirit_of_fight.open_skill_tree", "Open Skill Tree")
        add("key.spirit_of_fight.lock_on", "Lock On")
        
        // Sounds
        add("sound.spirit_of_fight.perfect_dodge", "Perfect Dodge")
        add("sound.spirit_of_fight.hard_block", "Hard Block")
        add("sound.spirit_of_fight.sharp_block", "Sharp Block")
        add("sound.spirit_of_fight.sharp_parry_1", "Sharp Parry")
        add("sound.spirit_of_fight.sharp_parry_2", "Sharp Parry")
        add("sound.spirit_of_fight.sharp_parry_3", "Sharp Parry")
        add("sound.spirit_of_fight.sharp_wield_1", "Sharp Equip")
        add("sound.spirit_of_fight.hard_wield_1", "Hard Equip")
        add("sound.spirit_of_fight.hard_under_attack_1", "Hard Hit")
        add("sound.spirit_of_fight.hard_under_attack_2", "Hard Hit")
        add("sound.spirit_of_fight.sharp_under_attack_1", "Sharp Hit")
        add("sound.spirit_of_fight.sharp_under_attack_2", "Sharp Hit")
        add("sound.spirit_of_fight.sharp_under_attack_3", "Sharp Hit")
        add("sound.spirit_of_fight.sharp_under_attack_4", "Sharp Hit")
        add("sound.spirit_of_fight.sharp_under_attack_5", "Sharp Hit")
        add("sound.spirit_of_fight.soft_block", "Soft Block")
        add("sound.spirit_of_fight.soft_under_attack_1", "Soft Hit")
        add("sound.spirit_of_fight.soft_under_attack_2", "Soft Hit")
        add("sound.spirit_of_fight.soft_under_attack_3", "Soft Hit")
        add("sound.spirit_of_fight.soft_under_attack_4", "Soft Hit")
        add("sound.spirit_of_fight.wooden_fish", "Wooden Fish Splat")
    }
}