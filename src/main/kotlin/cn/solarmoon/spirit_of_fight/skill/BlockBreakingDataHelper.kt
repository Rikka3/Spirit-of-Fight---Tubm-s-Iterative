package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spirit_of_fight.registry.common.SOFAttachments
import net.minecraft.world.entity.Entity

/**
 * Extension functions for accessing BlockBreakingData
 */
fun Entity.getBlockBreakingData() = getData(SOFAttachments.BLOCK_BREAKING)

fun Entity.setBlockBreakingData(data: BlockBreakingData) = setData(SOFAttachments.BLOCK_BREAKING, data)
