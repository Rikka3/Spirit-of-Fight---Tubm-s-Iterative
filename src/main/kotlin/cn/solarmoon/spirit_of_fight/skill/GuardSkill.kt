package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.physics.toVec3
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillTimeLine
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.component.body_binder.RigidBodyBinder
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.ai.behavior.MeleeAttack
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.random.Random

class GuardSkill(
    val blockAnimPlayer: PlayAnimationComponent,
    val blockHurtAnimPlayer: PlayAnimationComponent,
    val parryAnimIndexes: List<AnimIndex>,
    val bodyBinders: List<RigidBodyBinder>,
    val parryActiveTime: List<SkillTimeLine.Stamp> = listOf(SkillTimeLine.Stamp(0.0, 5.0, SkillTimeLine.Type.RUN)),
    val onSkillLifeCycle: List<SkillComponent> = listOf(),
    val onParryAnimLifeCycle: List<SkillComponent> = listOf(),
    val blockParticle: ParticleOptions = ParticleTypes.CRIT,
    val parryParticle: ParticleOptions = ParticleTypes.WHITE_ASH
): Skill() {

    val enableParry get() = parryAnims.isNotEmpty()
    var canParry = false
    lateinit var parryAnims: List<AnimInstance>

    fun animContains(name: String?): Boolean {
        return name in parryAnimIndexes.map { it.name }.toMutableList().apply { addAll(listOf(blockAnimPlayer.animIndex.name, blockHurtAnimPlayer.animIndex.name)) }
    }

    override fun onHurt(event: LivingIncomingDamageEvent) {
        val victim = event.entity
        if (victim !is IEntityAnimatable<*>) return
        val sourcePos = event.source.sourcePosition
        fun guard(guardBody: PhysicsCollisionObject) {
            event.isCanceled = true
            if (parryAnims.any { !it.isCancelled } || !blockHurtAnimPlayer.anim.isCancelled) return
            PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag().apply {
                val contactPos = guardBody.getPhysicsLocation(Vector3f()).apply { event.source.extraData?.manifoldId?.let { ManifoldPoints.getPositionWorldOnA(it, this) } }.toVec3()
                putDouble("x", contactPos.x)
                putDouble("y", contactPos.y)
                putDouble("z", contactPos.z)
                sourcePos?.let {
                    putBoolean("k", true)
                }
                putInt("op", 0)
            }))
            blockHurtAnimPlayer.anim.refresh()
            victim.animController.setAnimation(blockHurtAnimPlayer.anim, 0)
        }

        fun parry(guardBody: PhysicsCollisionObject) {
            event.isCanceled = true
            if (parryAnims.any { !it.isCancelled }) return
            getTargets().forEach { removeTarget(it) }
            event.source.entity?.let { addTarget(it) }
            val ordinal = Random.nextInt(parryAnims.size)
            PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag().apply {
                val contactPos = guardBody.getPhysicsLocation(Vector3f()).apply { event.source.extraData?.manifoldId?.let { ManifoldPoints.getPositionWorldOnA(it, this) } }.toVec3()
                putDouble("x", contactPos.x)
                putDouble("y", contactPos.y)
                putDouble("z", contactPos.z)
                putInt("op", 1)
                putInt("parry", ordinal)
            }))
            val anim = parryAnims[ordinal]
            victim.animController.setAnimation(anim, 0)
            event.isCanceled = true
        }

        physicsBodies.forEach {
            if (it.collideWithGroups != 0) {
                if (sourcePos != null) {
                    if (victim.canSee(sourcePos, 150.0)) {
                        if (canParry) parry(it) else guard(it)
                    }
                }
            }
        }
    }

    override fun onSync(data: CompoundTag, context: IPayloadContext) {
        val animatable = holder as? IEntityAnimatable<*> ?: return
        val op = data.getInt("op")
        when(op) {
            0 -> {
                val x = data.getDouble("x")
                val y = data.getDouble("y")
                val z = data.getDouble("z")
                repeat(15) {
                    level.addParticle(blockParticle, x, y, z, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5)
                }
                blockHurtAnimPlayer.anim.refresh()
                animatable.animController.setAnimation(blockHurtAnimPlayer.anim, 0)
            }
            1 -> {
                val ordinal = data.getInt("parry")
                val anim = parryAnims[ordinal]
                animatable.animController.setAnimation(anim, 0)
                val x = data.getDouble("x")
                val y = data.getDouble("y")
                val z = data.getDouble("z")
                repeat(15) {
                    level.addParticle(ParticleTypes.FIREWORK, x, y, z, -0.5 + Random.nextDouble(), -0.25 + Random.nextDouble(), -0.5 + Random.nextDouble())
                }
                repeat(7) {
                    level.addParticle(ParticleTypes.FLAME, x, y, z, -0.5 + Random.nextDouble(), -0.25 + Random.nextDouble(), -0.5 + Random.nextDouble())
                }
            }
        }
    }

    override fun onActive(): Boolean {
        val animatable = holder as? IEntityAnimatable<*> ?: return false

        bodyBinders.forEach {
            it.attach(this)
            it.body.flags.add("guard")
        }

        blockAnimPlayer.attach(this)
        blockAnimPlayer.anim.apply {
            shouldTurnBody = true

            onEvent<AnimEvent.SwitchOut> { event ->
                if (!animContains(event.next?.name)) {
                    end()
                }
            }
        }

        blockHurtAnimPlayer.playOnAttach = false
        blockHurtAnimPlayer.attach(this)
        blockHurtAnimPlayer.anim.apply {
            shouldTurnBody = true
            onEvent<AnimEvent.Completed> {
                holder.animController.setAnimation(blockAnimPlayer.anim, 0)
            }

            onEvent<AnimEvent.SwitchOut> {
                if (!animContains(it.next?.name)) {
                    end()
                }
            }
        }

        parryAnims = parryAnimIndexes.map {
            AnimInstance.create(animatable, it) {
                shouldTurnBody = true

                onEvent<AnimEvent.SwitchIn> {
                    onParryAnimLifeCycle.forEach { it.attach(this@GuardSkill) }
                }

                onEvent<AnimEvent.SwitchOut> {
                    if (!animContains(it.next?.name)) {
                         end()
                    }
                }

                onEvent<AnimEvent.Completed> {
                    holder.animController.setAnimation(blockAnimPlayer.anim, 0)
                }
            }
        }

        onSkillLifeCycle.forEach { it.attach(this) }
        return true
    }

    override fun onUpdate() {
        canParry = enableParry && timeline.match(parryActiveTime)
    }

    override fun onEnd() {
        blockAnimPlayer.anim.cancel()
        blockHurtAnimPlayer.anim.cancel()
        parryAnims.map { it.cancel() }
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<GuardSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                PlayAnimationComponent.CODEC.fieldOf("block_animation").forGetter { it.blockAnimPlayer },
                PlayAnimationComponent.CODEC.fieldOf("block_hurt_animation").forGetter { it.blockHurtAnimPlayer },
                AnimIndex.CODEC.listOf().optionalFieldOf("parry_anim_indexes", listOf()).forGetter { it.parryAnimIndexes },
                RigidBodyBinder.CODEC.listOf().fieldOf("bodies").forGetter { it.bodyBinders },
                SkillTimeLine.Stamp.CODEC.listOf().optionalFieldOf("parry_active_time", listOf()).forGetter { it.parryActiveTime },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_skill_life_cycle", listOf()).forGetter { it.onSkillLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_parry_anim_life_cycle", listOf()).forGetter { it.onParryAnimLifeCycle },
                ParticleTypes.CODEC.optionalFieldOf("block_particle", ParticleTypes.CRIT).forGetter { it.blockParticle },
                ParticleTypes.CODEC.optionalFieldOf("parry_particle", ParticleTypes.WHITE_ASH).forGetter { it.parryParticle }
            ).apply(it, ::GuardSkill)
        }
    }

}