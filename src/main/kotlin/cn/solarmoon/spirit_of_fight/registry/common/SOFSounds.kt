package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight

object SOFSounds {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val PERFECT_DODGE = SpiritOfFight.REGISTER.sound()
        .id("perfect_dodge")
        .build()

    @JvmStatic
    val HARD_BLOCK = SpiritOfFight.REGISTER.sound()
        .id("hard_block")
        .build()

    @JvmStatic
    val SHARP_BLOCK = SpiritOfFight.REGISTER.sound()
        .id("sharp_block")
        .build()

    @JvmStatic
    val SHARP_PARRY_1 = SpiritOfFight.REGISTER.sound()
        .id("sharp_parry_1")
        .build()

    @JvmStatic
    val SHARP_PARRY_2 = SpiritOfFight.REGISTER.sound()
        .id("sharp_parry_2")
        .build()

    @JvmStatic
    val SHARP_PARRY_3 = SpiritOfFight.REGISTER.sound()
        .id("sharp_parry_3")
        .build()

    @JvmStatic
    val SHARP_WIELD_1 = SpiritOfFight.REGISTER.sound()
        .id("sharp_wield_1")
        .build()

    @JvmStatic
    val HARD_WIELD_1 = SpiritOfFight.REGISTER.sound()
        .id("hard_wield_1")
        .build()

    @JvmStatic
    val HARD_UNDER_ATTACK_1 = SpiritOfFight.REGISTER.sound()
        .id("hard_under_attack_1")
        .build()

    @JvmStatic
    val HARD_UNDER_ATTACK_2 = SpiritOfFight.REGISTER.sound()
        .id("hard_under_attack_2")
        .build()

    @JvmStatic
    val SHARP_UNDER_ATTACK_1 = SpiritOfFight.REGISTER.sound()
        .id("sharp_under_attack_1")
        .build()

    @JvmStatic
    val SHARP_UNDER_ATTACK_2 = SpiritOfFight.REGISTER.sound()
        .id("sharp_under_attack_2")
        .build()

    @JvmStatic
    val SOFT_BLOCK = SpiritOfFight.REGISTER.sound()
        .id("soft_block")
        .build()

}