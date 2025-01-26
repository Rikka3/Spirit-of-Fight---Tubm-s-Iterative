package cn.solarmoon.spirit_of_fight.hit.type

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.entity.attack.getAttackedData
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.phys.thread.ClientPhysLevel
import cn.solarmoon.spark_core.phys.thread.getPhysLevel
import cn.solarmoon.spark_core.phys.thread.laterConsume
import cn.solarmoon.spark_core.phys.toDQuaternion
import cn.solarmoon.spark_core.phys.toDVector3
import cn.solarmoon.spark_core.phys.toQuaternionf
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spark_core.visual_effect.common.trail.Trail
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.hit.AttackStrength
import cn.solarmoon.spirit_of_fight.hit.setHitType
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.neoforged.fml.loading.FMLEnvironment
import org.joml.Quaterniond
import org.joml.Vector3f
import org.ode4j.ode.DBody
import org.ode4j.ode.DBox
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom
import java.awt.Color

abstract class BaseHitType: HitType {

    override fun whenAttackEntry(body: DBody) {
        val entity = body.owner as? Entity ?: return
        entity.level().playSound(null, entity.blockPosition().above(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.75f, 1f - strength.value * 0.5f / 3f)
    }

    override fun whenAttacking(body: DBody) {
        val entity = body.owner as? Entity ?: return
        val animatable = entity as? IEntityAnimatable<*> ?: return
        val box = (body.firstGeom as? DBox)?.baseCopy() ?: return
        val color = if (strength == AttackStrength.SUPER_HEAVY) Color.RED else Color.WHITE
        val level = entity.level()
        if (level.isClientSide) {
            level.getPhysLevel().scope.launch {
                SparkVisualEffects.TRAIL.refresh(box.uuid.toString()) {
                    val physPT = (level.getPhysLevel() as ClientPhysLevel).partialTicks.toFloat()
                    box.quaternion = animatable.getWorldBoneMatrix(body.name, it, physPT).getUnnormalizedRotation(Quaterniond()).toDQuaternion()
                    box.position = animatable.getWorldBonePivot(body.name, it, physPT).add(Vector3f(0f, 0f, -box.lengths.get2().toFloat() / 2).rotate(box.quaternion.toQuaternionf())).toDVector3()
                    Trail(box, Direction.Axis.Z, color).apply {
                        entity.weaponItem?.let { setTexture(it) }
                    }
                }
            }
        }
    }

    override fun whenAboutToAttack(
        o1: DGeom,
        o2: DGeom,
        buffer: DContactBuffer,
        attackSystem: AttackSystem,
        damageMultiply: Double
    ) {
        val entity = o1.body.owner as? Entity ?: return
        val contactPoint = buffer[0].contactGeom.pos
        entity.level().addParticle(ParticleTypes.SWEEP_ATTACK, contactPoint.get0(), contactPoint.get1(), contactPoint.get2(), 0.0, 0.0, 0.0)

        val target = o2.body.owner as? Entity ?: return
        target.getAttackedData()?.setHitType(this)
    }

    override fun whenTargetAttacked(
        o1: DGeom,
        o2: DGeom,
        buffer: DContactBuffer,
        attackSystem: AttackSystem,
        damageMultiply: Double
    ) {
        val entity = o1.body.owner as? Entity ?: return

        if (FMLEnvironment.dist.isClient && entity == Minecraft.getInstance().player) {
            SparkVisualEffects.CAMERA_SHAKE.shake(1 + strength.value, 0.5f + 0.25f * strength.value, 0.5f + strength.value)
        }
    }

    override fun getHitAnimation(
        target: IAnimatable<*>,
        strength: AttackStrength,
        boneName: String,
        posSide: Side,
        hitSide: Side
    ): AnimInstance? {
        val suffix = when (posSide) {
            Side.FRONT -> when (boneName) {
                "head" -> "$simpleName:head_$hitSide"
                "waist", "leftArm", "rightArm" -> "$simpleName:body_$hitSide"
                "leftLeg", "rightLeg" -> "$simpleName:leg_$hitSide"
                else -> null
            }
            else -> when (boneName) {
                "head", "waist", "leftArm", "rightArm" -> "${if (strength == AttackStrength.SUPER_HEAVY) "knockdown" else strength.toString().lowercase()}_all:upperbody_$posSide"
                "leftLeg", "rightLeg" -> "${if (strength == AttackStrength.SUPER_HEAVY) "knockdown" else strength.toString().lowercase()}_all:lowerbody_$posSide"
                else -> null
            }
        } ?: "$simpleName:${boneName}_$posSide#$hitSide"
        val animName = suffix.let { "Hit/$it" }
        return target.animations.getAnimation(animName)?.let { AnimInstance.create(target, animName, it) {
            rejectNewAnim = { indefensible || it?.name?.substringBefore("/") != "Hit" }

            onTick {
                val entity = (target.animatable as? Entity) ?: return@onTick
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, true)
                entity.putFlag(SparkFlags.DISARM, true)
                entity.putFlag(SparkFlags.SILENCE, true)
            }

            onEnd {
                val entity = (target.animatable as? Entity) ?: return@onEnd
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, false)
                entity.putFlag(SparkFlags.DISARM, false)
                entity.putFlag(SparkFlags.SILENCE, false)
            }
        } }
    }

    override fun getDeathAnimation(
        target: IAnimatable<*>,
        strength: AttackStrength,
        boneName: String,
        posSide: Side,
        hitSide: Side
    ): AnimInstance? {
        val suffix = when (posSide) {
            Side.FRONT -> when (boneName) {
                "head" -> "$simpleName:head_$hitSide"
                "waist", "leftArm", "rightArm" -> "$simpleName:body_$hitSide"
                "leftLeg", "rightLeg" -> "$simpleName:leg_$hitSide"
                else -> null
            }
            else -> when (boneName) {
                "head", "waist", "leftArm", "rightArm" -> "${if (strength == AttackStrength.SUPER_HEAVY) "knockdown" else strength.toString().lowercase()}_all:upperbody_$posSide"
                "leftLeg", "rightLeg" -> "${if (strength == AttackStrength.SUPER_HEAVY) "knockdown" else strength.toString().lowercase()}_all:lowerbody_$posSide"
                else -> null
            }
        } ?: "$simpleName:${boneName}_$posSide#$hitSide"
        val animName = suffix.let { "Death/$it" }
        return target.animations.getAnimation(animName)?.let { AnimInstance.create(target, animName, it) {
            rejectNewAnim = { indefensible || it?.name?.substringBefore("/") != "Death" }

            onTick {
                val entity = (target.animatable as? Entity) ?: return@onTick
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, true)
                entity.putFlag(SparkFlags.DISARM, true)
                entity.putFlag(SparkFlags.SILENCE, true)
            }

            onEnd {
                val entity = (target.animatable as? Entity) ?: return@onEnd
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, false)
                entity.putFlag(SparkFlags.DISARM, false)
                entity.putFlag(SparkFlags.SILENCE, false)
            }
        } }
    }

    override fun equals(other: Any?): Boolean {
        return (other as? HitType)?.registryKey == registryKey
    }

    override fun hashCode(): Int {
        return registryKey.hashCode()
    }

    override fun toString(): String {
        return registryKey.toString()
    }

}