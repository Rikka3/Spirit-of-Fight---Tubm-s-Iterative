package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.physics.host.PhysicsHost
import cn.solarmoon.spark_core.physics.toVec3
import cn.solarmoon.spark_core.registry.common.SparkSkillContext
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.component.body_binder.RigidBodyBinder
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spirit_of_fight.phys.CommonAttackCollisionCallback
import cn.solarmoon.spirit_of_fight.registry.common.SOFSkillContext
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

open class CommonAttackSkill(
    val animIndex: AnimIndex,
    val transitionTime: Int,
    val bodyBinders: List<RigidBodyBinder>,
    val onAnimLifeCycle: List<SkillComponent> = listOf(),
    val onPreFirstAttack: List<SkillComponent> = listOf(),
    val onPreAttack: List<SkillComponent> = listOf(),
    val onBoxActive: List<SkillComponent> = listOf(),
    val onSuccessHit: List<SkillComponent> = listOf(),
    val hitSound: List<SoundEvent> = listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value())
): Skill() {

    lateinit var animation: AnimInstance
    lateinit var bodies: List<PhysicsRigidBody>

    override fun onActive() {
        val animatable = holder as? IEntityAnimatable<*> ?: return
        bodyBinders.forEach {
            it.onBoxEntry {
                onBoxActive.forEach { it.attach(this) }
            }
            it.attach(this)
        }
        bodies = bodyBinders.map {
            it.create(holder as PhysicsHost)
        }
        blackBoard.write(SOFSkillContext.RIGID_BODIES, bodies)

        animation = AnimInstance.create(animatable, animIndex) {
            shouldTurnBody = true

            onEvent<AnimEvent.PhysicsTick> {
                bodyBinders.forEach {
                    it.physicsTick(time)
                }
            }

            onEvent<AnimEvent.SwitchIn> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onAnimLifeCycle.forEach { it.attach(this@CommonAttackSkill) }
            }

            onEvent<AnimEvent.Tick> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onAnimLifeCycle.forEach { it.tick() }
            }

            onEvent<AnimEvent.End> {
                end()
            }
        }

        bodies.forEach {
            it.addCollisionCallback(object : CommonAttackCollisionCallback() {
                override fun preAttack(
                    attacker: Entity,
                    target: Entity,
                    aBody: PhysicsCollisionObject,
                    bBody: PhysicsCollisionObject,
                    manifoldId: Long
                ) {
                    this@CommonAttackSkill.preAttack(attackSystem, attacker, target, aBody, bBody, manifoldId)
                }
            })
        }

        animatable.animController.setAnimation(animation, transitionTime)
    }

    open fun preAttack(
        attackSystem: AttackSystem,
        attacker: Entity,
        target: Entity,
        aBody: PhysicsCollisionObject,
        bBody: PhysicsCollisionObject,
        manifoldId: Long
    ) {
        if (level.isClientSide) return
        val isGuard = bBody.flags.contains("guard")

        blackBoard.write(SparkSkillContext.ENTITY_TARGET, target)
        blackBoard.write(SOFSkillContext.FIGHT_SPIRIT_MULTIPLY, if (isGuard) 0.5 else 1.0)

        val contactPos = Vector3f().apply { ManifoldPoints.getPositionWorldOnA(manifoldId, this) }.toVec3()
        PacketDistributor.sendToAllPlayers(SkillPayload(this@CommonAttackSkill, CompoundTag().apply {
            putDouble("x", contactPos.x)
            putDouble("y", contactPos.y)
            putDouble("z", contactPos.z)
            putInt("id", target.id)
            putBoolean("guard", isGuard)

            if (attackSystem.attackedEntities.isEmpty()) {
                onPreFirstAttack.forEach { it.attach(this@CommonAttackSkill) }
                putBoolean("first", true)
            }
        }))

        onPreAttack.forEach { it.attach(this) }
        if (!isGuard) onSuccessHit.forEach { it.attach(this) }

        target.hurtData?.context?.put("hitSound", hitSound)
    }

    override fun sync(data: CompoundTag, context: IPayloadContext) {
        val level = context.player().level()
        val isGuard = data.getBoolean("guard")
        val x = data.getDouble("x")
        val y = data.getDouble("y")
        val z = data.getDouble("z")

        level.getEntity(data.getInt("id"))?.let { blackBoard.write(SparkSkillContext.ENTITY_TARGET, it) }
        if (data.getBoolean("first")) onPreFirstAttack.forEach { it.attach(this) }
        blackBoard.write(SOFSkillContext.PARTICLE_POSITION, Vec3(x, y ,z))
        onPreAttack.forEach { it.attach(this) }

        if (!isGuard) {
            onSuccessHit.forEach { it.attach(this) }
        }
    }

    override fun onEnd() {
        animation.cancel()
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<CommonAttackSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                AnimIndex.CODEC.fieldOf("anim_index").forGetter { it.animIndex },
                Codec.INT.fieldOf("transition_time").forGetter { it.transitionTime },
                RigidBodyBinder.CODEC.listOf().fieldOf("bodies").forGetter { it.bodyBinders },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_anim_life_cycle", listOf()).forGetter { it.onAnimLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_pre_first_attack", listOf()).forGetter { it.onPreFirstAttack },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_pre_attack", listOf()).forGetter { it.onPreAttack },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_box_active", listOf()).forGetter { it.onBoxActive },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_success_hit", listOf()).forGetter { it.onSuccessHit },
                SoundEvent.DIRECT_CODEC.listOf().optionalFieldOf("hit_sound", listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value())).forGetter { it.hitSound }
            ).apply(it, ::CommonAttackSkill)
        }
    }

}