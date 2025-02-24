package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.physics.toVec3
import cn.solarmoon.spark_core.registry.common.SparkSkillContext
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.component.body_binder.RigidBodyBinder
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spirit_of_fight.registry.common.SOFSkillContext
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.random.Random
import kotlin.ranges.contains

class GuardSkill(
    val blockAnimIndex: AnimIndex,
    val transitionTime: Int,
    val blockHurtAnimIndex: AnimIndex,
    val parryAnimIndexes: List<AnimIndex>,
    val bodyBinders: List<RigidBodyBinder>,
    val parryActiveTime: List<Vec2> = listOf(Vec2(0f, 5.0f)),
    val onSkillLifeCycle: List<SkillComponent> = listOf(),
    val onBlockAnimLifeCycle: List<SkillComponent> = listOf(),
    val onHurtAnimLifeCycle: List<SkillComponent> = listOf(),
    val onParryAnimLifeCycle: List<SkillComponent> = listOf(),
    val blockParticle: ParticleOptions = ParticleTypes.CRIT,
    val parryParticle: ParticleOptions = ParticleTypes.WHITE_ASH
): Skill() {

    val enableParry get() = parryAnims.isNotEmpty()
    var canParry = false
    lateinit var bodies: List<PhysicsRigidBody>
    lateinit var guardAnim: AnimInstance
    lateinit var guardHurtAnim: AnimInstance
    lateinit var parryAnims: List<AnimInstance>

    fun animContains(name: String?): Boolean {
        return name in parryAnimIndexes.map { it.name }.toMutableList().apply { addAll(listOf(blockAnimIndex.name, blockHurtAnimIndex.name)) }
    }

    override fun onEvent(event: Event) {
        if (event is LivingIncomingDamageEvent) {
            val victim = event.entity
            if (victim !is IEntityAnimatable<*>) return
            val damagedBody = event.source.extraData?.damagedBody
            val sourcePos = event.source.sourcePosition
            blackBoard.write(SparkSkillContext.DAMAGE_SOURCE, event.source)
            if (victim == holder) {

                fun guard(guardBody: PhysicsRigidBody) {
                    event.isCanceled = true
                    if (parryAnims.any { !it.isCancelled }) return
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
                    guardHurtAnim.refresh()
                    victim.animController.setAnimation(guardHurtAnim, 0)
                }

                fun parry(guardBody: PhysicsRigidBody) {
                    event.isCanceled = true
                    if (parryAnims.any { !it.isCancelled }) return
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

                bodies.forEach {
                    if (it.collideWithGroups != 0) {
                        if (damagedBody != null) {
                            if (it == damagedBody) {
                                if (canParry) parry(it) else guard(it)
                            }
                        } else if (sourcePos != null) {
                            if (victim.canSee(sourcePos, 150.0)) {
                                if (canParry) parry(it) else guard(it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun sync(data: CompoundTag, context: IPayloadContext) {
        val animatable = holder as? IEntityAnimatable<*> ?: return
        val op = data.getInt("op")
        when(op) {
            0 -> {
                val x = data.getDouble("x")
                val y = data.getDouble("y")
                val z = data.getDouble("z")
                blackBoard.write(SOFSkillContext.PARTICLE_POSITION, Vec3(x, y ,z))

                guardHurtAnim.refresh()
                animatable.animController.setAnimation(guardHurtAnim, 0)
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

    override fun onActive() {
        val animatable = holder as? IEntityAnimatable<*> ?: return
        val entity = animatable.animatable

        bodies = bodyBinders.map {
            it.attach(this)
            it.create(entity).apply { flags.add("guard") }
        }

        guardAnim = AnimInstance.create(animatable, blockAnimIndex) {
            shouldTurnBody = true

            onEvent<AnimEvent.SwitchIn> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onBlockAnimLifeCycle.forEach { it.attach(this@GuardSkill) }
            }

            onEvent<AnimEvent.Tick> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onBlockAnimLifeCycle.forEach { it.tick() }
            }

            onEvent<AnimEvent.SwitchOut> { event ->
                if (!animContains(event.next?.name)) {
                    end()
                }
            }
        }

        guardHurtAnim = AnimInstance.create(animatable, blockHurtAnimIndex) {
            shouldTurnBody = true

            onEvent<AnimEvent.SwitchIn> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onHurtAnimLifeCycle.forEach { it.attach(this@GuardSkill) }
            }

            onEvent<AnimEvent.Tick> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onHurtAnimLifeCycle.forEach { it.tick() }
            }

            onEvent<AnimEvent.Completed> {
                holder.animController.setAnimation(guardAnim, 0)
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
                    blackBoard.write(SparkSkillContext.TIME, time)
                    onParryAnimLifeCycle.forEach { it.attach(this@GuardSkill) }
                }

                onEvent<AnimEvent.Tick> {
                    blackBoard.write(SparkSkillContext.TIME, time)
                    onParryAnimLifeCycle.forEach { it.tick() }
                }

                onEvent<AnimEvent.SwitchOut> {
                    if (!animContains(it.next?.name)) {
                         end()
                    }
                }

                onEvent<AnimEvent.Completed> {
                    holder.animController.setAnimation(guardAnim, 0)
                }
            }
        }

        animatable.animController.setAnimation(guardAnim, transitionTime)

        onSkillLifeCycle.forEach { it.attach(this) }
    }

    override fun onUpdate() {
        val animatable = holder as? IEntityAnimatable<*> ?: return

        canParry = enableParry && (parryActiveTime.isEmpty() || parryActiveTime.any { runTime.toDouble() in it.x..it.y })

        val time = animatable.animController.getPlayingAnim()?.time ?: runTime.toDouble()
        bodyBinders.forEach { it.physicsTick(time) }

        onSkillLifeCycle.forEach { it.tick() }
    }

    override fun onEnd() {
        guardAnim.cancel()
        guardHurtAnim.cancel()
        parryAnims.map { it.cancel() }
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<GuardSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                AnimIndex.CODEC.fieldOf("block_anim_index").forGetter { it.blockAnimIndex },
                Codec.INT.fieldOf("transition_time").forGetter { it.transitionTime },
                AnimIndex.CODEC.fieldOf("block_hurt_anim_index").forGetter { it.blockHurtAnimIndex },
                AnimIndex.CODEC.listOf().optionalFieldOf("parry_anim_indexes", listOf()).forGetter { it.parryAnimIndexes },
                RigidBodyBinder.CODEC.listOf().fieldOf("bodies").forGetter { it.bodyBinders },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("parry_active_time", listOf(Vec2(0.0f, 5.0f))).forGetter { it.parryActiveTime },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_skill_life_cycle", listOf()).forGetter { it.onSkillLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_block_anim_life_cycle", listOf()).forGetter { it.onBlockAnimLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_hurt_anim_life_cycle", listOf()).forGetter { it.onHurtAnimLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_parry_anim_life_cycle", listOf()).forGetter { it.onParryAnimLifeCycle },
                ParticleTypes.CODEC.optionalFieldOf("block_particle", ParticleTypes.CRIT).forGetter { it.blockParticle },
                ParticleTypes.CODEC.optionalFieldOf("parry_particle", ParticleTypes.WHITE_ASH).forGetter { it.parryParticle }
            ).apply(it, ::GuardSkill)
        }
    }

}