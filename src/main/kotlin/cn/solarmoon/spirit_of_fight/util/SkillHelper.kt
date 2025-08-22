package cn.solarmoon.spirit_of_fight.util

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.SparkHurtDatas
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.poise_system.HitType
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.math.Vector3f
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

object SkillHelper {

    fun guard(damageSource: DamageSource, guardBody: PhysicsCollisionObject, onGuard: (PhysicsCollisionObject?, Vec3) -> Unit = {_, _ -> }) {
        val damagedBody = damageSource.extraData.read(SparkHurtDatas.COLLISION)?.damagedBody
        val sourcePos = damageSource.sourcePosition

        if (guardBody.collideWithGroups != 0) {
            if (damagedBody != null && damagedBody == guardBody) {
                val hitPos = damagedBody.getPhysicsLocation(Vector3f())
                    .apply { damageSource.extraData.read(SparkHurtDatas.COLLISION)?.manifoldId?.let { ManifoldPoints.getPositionWorldOnA(it, this) } }
                    .toVec3()
                onGuard.invoke(damagedBody, hitPos)
            } else if (sourcePos != null) {
                onGuard.invoke(null, guardBody.getPhysicsLocation(Vector3f()).toVec3())
            }
        }
    }

    fun grabToBone(animatable: IEntityAnimatable<*>, bone: String, offset: Vec3) {
        val entity = animatable.animatable
        entity.grabManager.getGrabs().forEach {
            if (!animatable.animLevel.isClientSide) {
                val pos = animatable.getWorldBonePivot(bone, offset).toVec3()
                it.setPos(pos)
            }
        }
    }

    fun createAnimByDirection(animatable: IEntityAnimatable<*>, path: ResourceLocation, animBaseName: String, default: MoveDirection): AnimInstance {
        val direction = animatable.animatable.moveDirection ?: default
        return AnimInstance.create(animatable, AnimIndex(path, "${animBaseName}.${direction.toString().lowercase()}"))
    }

    fun sofCommonAttack(attacker: Entity, target: Entity, hitType: HitType) {
        val entity = attacker
        target.hurtData.write(EntityHitApplier.HIT_TYPE, hitType)
        if (entity.level().isClientSide) return
        if (target is LivingEntity) {
            if (entity is Player) {
                entity.attack(target)
            } else if (entity is LivingEntity) {
                entity.doHurtTarget(target)
            }
        }
    }

}