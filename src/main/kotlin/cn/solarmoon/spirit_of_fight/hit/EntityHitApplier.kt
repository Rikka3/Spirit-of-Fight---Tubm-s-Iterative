package cn.solarmoon.spirit_of_fight.hit

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.camera.isCameraLocked
import cn.solarmoon.spark_core.camera.setCameraLock
import cn.solarmoon.spark_core.entity.getLateralSide
import cn.solarmoon.spark_core.entity.getSide
import cn.solarmoon.spark_core.util.Key
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spirit_of_fight.event.GetHitAnimationEvent
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.registry.common.SOFTypedAnimations
import com.jme3.math.Vector3f
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

object EntityHitApplier {

    val HIT_TYPE = Key.create<HitType>("hit_type")

    @SubscribeEvent
    private fun onHit(event: LivingDamageEvent.Post) {
        val victim = event.entity
        if (victim.isKnockedDown) return
        if (event.newDamage <= 0f) return
        if (victim !is IEntityAnimatable<*>) return
        val source = event.source
        val sourcePos = source.sourcePosition ?: return
        val attackData = source.extraData ?: return

        val hitType = attackData.blackBoard.read(HIT_TYPE) ?: return
        if (hitType.level == HitLevel.WHEN_NOT_PLAYING_SKILLS && victim.isPlayingSkill) return
        val hitSide = victim.getLateralSide(attackData.damagedBody.getPhysicsLocation(Vector3f()).toVec3())
        val posSide = victim.getSide(sourcePos)
        attackData.damagedBody.let {
            val boneName = it.name
            NeoForge.EVENT_BUS.post(GetHitAnimationEvent(victim, hitType, boneName, posSide, hitSide)).resultHitAnim?.apply {
                if (exist()) {
                    play(victim, 0)
                    playToClient(victim.id, 0)
                } else SparkCore.LOGGER.warn("${victim.type} 缺少受击动画：${index}")
            }
        }
    }

    @SubscribeEvent
    private fun getHitAnim(event: GetHitAnimationEvent) {
        val animName = SOFHitTypes.getHitAnimation(event.hitType.name, event.boneName, event.posSide, event.hitSide)
        SOFTypedAnimations.HIT_ANIMS[animName]?.let {
            if (event.animatable.model.bones.keys.containsAll(setOf("head", "waist", "leftArm", "rightArm", "leftLeg", "rightLeg"))) {
                event.resultHitAnim = it.get()
            }
        }
    }

    fun hitAnimDoFreeze(anim: AnimInstance) {
        anim.apply {
            onEvent<AnimEvent.SwitchIn> {
                val entity = holder.animatable as? Entity ?: return@onEvent
                if (index.name.contains("knockdown")) entity.isKnockedDown = true
                entity.isHitting = true
                entity.setCameraLock(true)
                entity.preInput.disable()
            }

            onEvent<AnimEvent.Tick> {
                val entity = holder.animatable as? Entity ?: return@onEvent
                if (time >= origin.animationLength / 2) {
                    entity.preInput.allowInput(SOFPreInputs.DODGE)
                }
            }

            onEvent<AnimEvent.End> {
                val entity = holder.animatable as? Entity ?: return@onEvent
                entity.preInput.disallowInput(SOFPreInputs.DODGE)
                entity.preInput.enable()
                entity.isHitting = false
                entity.setCameraLock(false)
                if (index.name.contains("knockdown")) entity.isKnockedDown = false
            }
        }
    }
//
//    @SubscribeEvent
//    private fun onDeath(event: LivingDeathEvent) {
//        val victim = event.entity
//        if (victim !is IEntityAnimatable<*>) return
//        val source = event.source
//        val sourcePos = source.sourcePosition ?: return
//        val attackData = source.extraData ?: return
//        val hitType = attackData.getHitType() ?: return
//        val strength = hitType.strength
//        val side = victim.getLateralSide(attackData.damageBox.position.toVec3())
//        val posSide = victim.getSide(sourcePos)
//        attackData.damagedBody?.let {
//            val boneName = it.name
//            val hitAnimation = hitType.getDeathAnimation(victim, strength, boneName, posSide, side) ?: return
//            victim.animController.setAnimation(hitAnimation, 0)
//            PacketDistributor.sendToAllPlayers(HitAnimPayload(victim.id, hitType.id, strength, boneName, posSide, side, true))
//        }
//    }

    @SubscribeEvent
    private fun fall(event: LivingDamageEvent.Post) {
        val entity = event.entity
        if (entity.isKnockedDown) return
        if (entity !is IEntityAnimatable<*>) return
        if (event.source.typeHolder().`is`(DamageTypes.FALL) && event.newDamage > 0) {
            SOFTypedAnimations.PLAYER_HIT_LANDING.get().apply {
                play(entity, 0)
                playToClient(entity.id, 0)
            }
        }
    }

}