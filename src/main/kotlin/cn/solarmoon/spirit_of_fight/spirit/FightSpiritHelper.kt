package cn.solarmoon.spirit_of_fight.spirit

import cn.solarmoon.spirit_of_fight.registry.common.SOFAttachments
import net.minecraft.world.entity.Entity

fun Entity.getFightSpirit() = getData(SOFAttachments.FIGHT_SPIRIT)

fun Entity.setFightSpirit(spirit: FightSpirit) = setData(SOFAttachments.FIGHT_SPIRIT, spirit)

//fun FightSpirit.commonAdd(o1: DGeom, o2: DGeom, mul: Double) {
//    val holder = o1.body.owner as? Entity ?: return
//    val target = o2.body.owner as? Entity ?: return
//    val level = target.level()
//    if (level.isClientSide) return
//    var mul = mul
//    if (o2.body.type == SOFBodyTypes.GUARD.get()) mul /= 2
//    addStage(mul)
//    syncToClient(holder.id)
//}