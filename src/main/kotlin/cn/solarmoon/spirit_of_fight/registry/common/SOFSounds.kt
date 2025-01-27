package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight

object SOFSounds {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val HARD_BLOCK = SpiritOfFight.REGISTER.sound()
        .id("hard_block")
        .build()

    @JvmStatic
    val SHARP_BLOCK = SpiritOfFight.REGISTER.sound()
        .id("sharp_block")
        .build()

    @JvmStatic
    val SHARP_PARRY = SpiritOfFight.REGISTER.sound()
        .id("sharp_parry")
        .build()

    @JvmStatic
    val SOFT_BLOCK = SpiritOfFight.REGISTER.sound()
        .id("soft_block")
        .build()

}