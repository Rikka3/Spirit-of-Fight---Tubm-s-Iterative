package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import net.minecraft.resources.ResourceLocation

object SOFSkillTypes {

    val SWORD_DEFAULT_COMBO_1 = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.combo_1")
    val SWORD_DEFAULT_COMBO_2 = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.combo_2")
    val SWORD_DEFAULT_COMBO_3 = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.combo_3")
    val SWORD_DEFAULT_SHIELD_COMBO_2 = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.shield.combo_2")
    val SWORD_DEFAULT_SHIELD_COMBO_3 = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.shield.combo_3")
    val SWORD_DEFAULT_SPRINT_ATTACK = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.sprint_attack")
    val SWORD_SPECIAL_COMBO_1 = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.combo_1")
    val SWORD_SPECIAL_COMBO_2 = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.combo_2")
    val SWORD_SPECIAL_COMBO_3 = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.combo_3")
    val SWORD_SPECIAL_SHIELD_COMBO_2 = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.shield.combo_2")
    val SWORD_SPECIAL_SHIELD_COMBO_3 = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.shield.combo_3")
    val SWORD_SPECIAL_SPRINT_ATTACK = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.sprint_attack")
    val SWORD_SHIELD_COMBO_C1 = sofKey("sword_shield_combo_c1")
    val SWORD_JUMP_ATTACK = sofKey("sword_jump_attack")
    val SWORD_DODGE = sofKey("sword.dodge")
    val SWORD_BLOCK = sofKey("sword.block")
    val SWORD_SHIELD_BLOCK = sofKey("sword.shield.block")
    val SWORD_SWITCH_ATTACK = sofKey("sword.switch_attack")

    val HAMMER_DEFAULT_COMBO_1 = sofKey("hammer.${WieldStyle.DEFAULT.serializedName}.combo_1")
    val HAMMER_DEFAULT_COMBO_2 = sofKey("hammer.${WieldStyle.DEFAULT.serializedName}.combo_2")
    val HAMMER_DEFAULT_COMBO_3 = sofKey("hammer.${WieldStyle.DEFAULT.serializedName}.combo_3")
    val HAMMER_DEFAULT_SPRINT_ATTACK = sofKey("hammer.${WieldStyle.DEFAULT.serializedName}.sprint_attack")
    val HAMMER_SPECIAL_COMBO_1 = sofKey("hammer.${WieldStyle.SPECIAL.serializedName}.combo_1")
    val HAMMER_SPECIAL_COMBO_2 = sofKey("hammer.${WieldStyle.SPECIAL.serializedName}.combo_2")
    val HAMMER_SPECIAL_COMBO_3 = sofKey("hammer.${WieldStyle.SPECIAL.serializedName}.combo_3")
    val HAMMER_SPECIAL_SPRINT_ATTACK = sofKey("hammer.${WieldStyle.SPECIAL.serializedName}.sprint_attack")
    val HAMMER_JUMP_ATTACK = sofKey("sword_jump_attack")
    val HAMMER_DODGE = sofKey("hammer.dodge")
    val HAMMER_BLOCK = sofKey("hammer.block")
    val HAMMER_SWITCH_ATTACK = sofKey("hammer.switch_attack")

    val SPEAR_DEFAULT_COMBO_1 = sofKey("spear.${WieldStyle.DEFAULT.serializedName}.combo_1")
    val SPEAR_DEFAULT_COMBO_2 = sofKey("spear.${WieldStyle.DEFAULT.serializedName}.combo_2")
    val SPEAR_DEFAULT_COMBO_3 = sofKey("spear.${WieldStyle.DEFAULT.serializedName}.combo_3")
    val SPEAR_SPECIAL_COMBO_1 = sofKey("spear.${WieldStyle.SPECIAL.serializedName}.combo_1")
    val SPEAR_SPECIAL_COMBO_2 = sofKey("spear.${WieldStyle.SPECIAL.serializedName}.combo_2")
    val SPEAR_SPECIAL_COMBO_3 = sofKey("spear.${WieldStyle.SPECIAL.serializedName}.combo_3")
    val SPEAR_SWITCH_ATTACK = sofKey("spear.switch_attack")

    val GLOVES_DEFAULT_COMBO_1 = sofKey("gloves.${WieldStyle.DEFAULT.serializedName}.combo_1")
    val GLOVES_DEFAULT_COMBO_2 = sofKey("gloves.${WieldStyle.DEFAULT.serializedName}.combo_2")
    val GLOVES_DEFAULT_COMBO_3 = sofKey("gloves.${WieldStyle.DEFAULT.serializedName}.combo_3")
    val GLOVES_DEFAULT_SPRINT_ATTACK = sofKey("gloves.${WieldStyle.DEFAULT.serializedName}.sprint_attack")
    val GLOVES_SPECIAL_COMBO_1 = sofKey("gloves.${WieldStyle.SPECIAL.serializedName}.combo_1")
    val GLOVES_SPECIAL_COMBO_2 = sofKey("gloves.${WieldStyle.SPECIAL.serializedName}.combo_2")
    val GLOVES_SPECIAL_COMBO_3 = sofKey("gloves.${WieldStyle.SPECIAL.serializedName}.combo_3")
    val GLOVES_SPECIAL_SPRINT_ATTACK = sofKey("gloves.${WieldStyle.SPECIAL.serializedName}.sprint_attack")
    val GLOVES_DODGE = sofKey("gloves.dodge")
    val GLOVES_BLOCK = sofKey("gloves.block")
    val GLOVES_SWITCH_ATTACK = sofKey("gloves.switch_attack")

    fun sofKey(id: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id)
}