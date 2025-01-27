package cn.solarmoon.spirit_of_fight.hit

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.getExtraData
import cn.solarmoon.spark_core.entity.getLateralSide
import cn.solarmoon.spark_core.entity.getSide
import cn.solarmoon.spark_core.phys.toVec3
import cn.solarmoon.spirit_of_fight.registry.common.SOFTypedAnimations
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.network.PacketDistributor

object HitAnimationApplier {

    @SubscribeEvent
    private fun onHit(event: LivingDamageEvent.Post) {
        val victim = event.entity

        if (event.newDamage <= 0f) return
        if (victim !is IEntityAnimatable<*>) return
        val source = event.source
        val sourcePos = source.sourcePosition ?: return
        val attackData = source.getExtraData() ?: let {
            playRandomLightHitAnim(victim, source)
            return
        }

        val hitType = attackData.getHitType() ?: return
        val strength = hitType.strength
        val side = victim.getLateralSide(attackData.damageBox.position.toVec3())
        val posSide = victim.getSide(sourcePos)
        attackData.damagedBody?.let {
            val boneName = it.name
            val hitAnimation = hitType.getHitAnimation(victim, strength, boneName, posSide, side) ?: return
            victim.animController.setAnimation(hitAnimation, 0)
            PacketDistributor.sendToAllPlayers(HitAnimPayload(victim.id, hitType.id, strength, boneName, posSide, side, false))
        }
    }

    @SubscribeEvent
    private fun onDeath(event: LivingDeathEvent) {
        val victim = event.entity
        if (victim !is IEntityAnimatable<*>) return
        val source = event.source
        val sourcePos = source.sourcePosition ?: return
        val attackData = source.getExtraData() ?: return
        val hitType = attackData.getHitType() ?: return
        val strength = hitType.strength
        val side = victim.getLateralSide(attackData.damageBox.position.toVec3())
        val posSide = victim.getSide(sourcePos)
        attackData.damagedBody?.let {
            val boneName = it.name
            val hitAnimation = hitType.getDeathAnimation(victim, strength, boneName, posSide, side) ?: return
            victim.animController.setAnimation(hitAnimation, 0)
            PacketDistributor.sendToAllPlayers(HitAnimPayload(victim.id, hitType.id, strength, boneName, posSide, side, true))
        }
    }

    @SubscribeEvent
    private fun fall(event: LivingDamageEvent.Post) {
        val entity = event.entity
        if (entity !is IEntityAnimatable<*>) return
        if (event.source.typeHolder().`is`(DamageTypes.FALL) && event.newDamage > 0) {
            SOFTypedAnimations.HIT_LANDING.get().play(entity, 0)
            SOFTypedAnimations.HIT_LANDING.get().syncToClient(entity.id, 0)
        }
    }

}