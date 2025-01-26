package cn.solarmoon.spirit_of_fight.hit

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.AttackedData
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spirit_of_fight.hit.type.HitType
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.neoforged.neoforge.network.PacketDistributor

fun AttackedData.getHitType() = extraData.getInt("hitType").let { SOFRegistries.HIT_TYPE.byId(it) }

fun AttackedData.setHitType(hitType: HitType) {
    extraData.putInt("hitType", hitType.id)
}

fun playRandomLightHitAnim(victim: IEntityAnimatable<*>, source: DamageSource) {
    if (listOf(DamageTypes.IN_WALL, DamageTypes.IN_FIRE, DamageTypes.STARVE, DamageTypes.CAMPFIRE, DamageTypes.FREEZE, DamageTypes.LAVA, DamageTypes.WITHER).any { source.`is`(it) }) return
    val hitType = listOf(SOFHitTypes.LIGHT_SWIPE.get(), SOFHitTypes.LIGHT_CHOP.get(), SOFHitTypes.LIGHT_STAB.get()).random()
    val boneName = listOf("rightArm", "leftArm", "waist").random()
    val strength = listOf(AttackStrength.LIGHT).random()
    val posSide = Side.entries.random()
    val side = listOf(Side.LEFT, Side.RIGHT).random()
    val hitAnimation = hitType.getHitAnimation(victim, strength, boneName, posSide, side) ?: return
    if (!victim.animations.hasAnimation(hitAnimation.name)) return
    victim.animController.setAnimation(hitAnimation, 0)
    PacketDistributor.sendToAllPlayers(HitAnimPayload(victim.animatable.id, hitType.id, strength, boneName, posSide, side, false))
}

