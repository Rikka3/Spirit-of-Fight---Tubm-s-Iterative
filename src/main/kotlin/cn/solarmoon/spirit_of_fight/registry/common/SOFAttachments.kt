package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.BlockBreakingData
import cn.solarmoon.spirit_of_fight.spirit.FightSpirit

object SOFAttachments {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val FIGHT_SPIRIT = SpiritOfFight.REGISTER.attachment<FightSpirit>()
        .id("fight_spirit")
        .defaultValue { FightSpirit() }
        .serializer { it.serialize(FightSpirit.CODEC) }
        .build()

    @JvmStatic
    val BLOCK_BREAKING = SpiritOfFight.REGISTER.attachment<BlockBreakingData>()
        .id("block_breaking")
        .defaultValue { BlockBreakingData() }
        .serializer { it.serialize(BlockBreakingData.CODEC) }
        .build()

}