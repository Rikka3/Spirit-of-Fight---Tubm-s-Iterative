package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.camera.setCameraLock
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.js.getMember
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.physics.collision.PhysicsEvent
import cn.solarmoon.spark_core.physics.onEvent
import cn.solarmoon.spark_core.physics.presets.callback.AttackCollisionCallback
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillEvent
import cn.solarmoon.spark_core.skill.read
import cn.solarmoon.spark_core.skill.readNonNull
import cn.solarmoon.spark_core.skill.readNullable
import cn.solarmoon.spirit_of_fight.poise_system.HitType
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.world.entity.Entity
import org.mozilla.javascript.Context
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.Scriptable

class AttackSkill: Skill() {

    init {
        onEvent<SkillEvent.Init> {
            val holder = holder as? IEntityAnimatable<*> ?: return@onEvent
            val entity = holder.animatable

            val anim = config.readNonNull<AnimInstance>("animation")
            val attackBodies = config.read("attack_bodies", NativeArray(arrayOf())).map { it as PhysicsCollisionObject }
            val cameraShakeValue = config.readNullable<NativeArray>("attacker_camera_shake_value")?.toVec3()
            val spiritIncrease = config.read("fight_spirit_increase", 25.0).toInt()
            val hitType = config.readNonNull<Scriptable>("hit_type").let {
                HitType(
                    Context.toString(it.getMember("name")),
                    Context.toNumber(it.getMember("poise_damage")).toInt(),
                    Context.toNumber(it.getMember("fight_spirit_damage")).toInt()
                )
            }
            val cameraLockOnCollisionActive = config.read("camera_lock_on_collision_active", true)
            val soundOnCollisionActive = config.readNullable<Scriptable>("sound_on_collision_active")?.let {
                Triple(
                    Context.toString(it.getMember("sound")),
                    Context.toNumber(it.getMember("volume")).toFloat(),
                    Context.toNumber(it.getMember("pitch")).toFloat()
                )
            }

            val attackSys = AttackSystem()
            attackBodies.forEach {
                it.addCollisionCallback(object : AttackCollisionCallback {
                    override val attackSystem: AttackSystem = attackSys
                    override fun preAttack(
                        isFirst: Boolean,
                        attacker: Entity,
                        target: Entity,
                        aBody: PhysicsCollisionObject,
                        bBody: PhysicsCollisionObject,
                        manifoldId: Long
                    ) {
                        if (level.isClientSide) return
                        targetPool.addTarget(target, true)
                        if (isFirst && cameraShakeValue != null) SparkVisualEffects.CAMERA_SHAKE.shakeToClient(attacker, cameraShakeValue.x.toInt(), cameraShakeValue.y.toFloat(), cameraShakeValue.z.toFloat())
                        attacker.getFightSpirit().apply {
                            addStage(spiritIncrease)
                            syncToClient(attacker.id)
                        }
                    }

                    override fun doAttack(
                        attacker: Entity,
                        target: Entity,
                        aBody: PhysicsCollisionObject,
                        bBody: PhysicsCollisionObject,
                        manifoldId: Long
                    ): Boolean {
                        SkillHelper.sofCommonAttack(attacker, target, hitType)
                        return super.doAttack(attacker, target, aBody, bBody, manifoldId)
                    }

                    override fun postAttack(
                        attacker: Entity,
                        target: Entity,
                        aBody: PhysicsCollisionObject,
                        bBody: PhysicsCollisionObject,
                        manifoldId: Long
                    ) {
                        targetPool.removeTarget(target, true)
                    }
                })

                it.onEvent<PhysicsEvent.OnCollisionActive> {
                    if (cameraLockOnCollisionActive) entity.setCameraLock(true)
                }
            }

            onEvent<SkillEvent.Active> {

            }

        }
    }

}