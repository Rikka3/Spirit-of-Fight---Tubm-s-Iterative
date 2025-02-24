package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.physics.toVec3
import cn.solarmoon.spark_core.registry.common.SparkSkillContext
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.PreInputReleaseComponent
import cn.solarmoon.spark_core.skill.component.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.component.PreventYRotComponent
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.component.body_binder.RigidBodyBinder
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spirit_of_fight.registry.common.SOFSkillContext
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class SuperArmorBlockSkill(
    val animIndex: AnimIndex,
    val transitionTime: Int,
    val bodyBinders: List<RigidBodyBinder>,
    val accessibleAnims: List<String> = listOf(),
    val onAnimLifeCycle: List<SkillComponent> = listOf(),
    val onBlock: List<SkillComponent> = listOf()
): Skill() {

    lateinit var bodies: List<PhysicsRigidBody>
    lateinit var blockAnim: AnimInstance

    override fun onActive() {
        val animatable = holder as? IEntityAnimatable<*> ?: return
        val entity = animatable.animatable

        bodies = bodyBinders.map {
            it.attach(this)
            it.create(entity).apply { flags.add("guard") }
        }

        blockAnim = AnimInstance.create(animatable, animIndex) {
            shouldTurnBody = true

            rejectNewAnim = { next ->
                next?.name !in accessibleAnims
            }

            onEvent<AnimEvent.SwitchIn> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onAnimLifeCycle.forEach { it.attach(this@SuperArmorBlockSkill) }
            }

            onEvent<AnimEvent.Tick> {
                blackBoard.write(SparkSkillContext.TIME, time)
                blackBoard.write(SOFSkillContext.PARTICLE_POSITION, entity.position())
                onAnimLifeCycle.forEach { it.tick() }
            }

            onEvent<AnimEvent.PhysicsTick> {
                bodyBinders.forEach { it.physicsTick(time) }
            }

            onEvent<AnimEvent.End> {
                end()
            }
        }

        animatable.animController.setAnimation(blockAnim, transitionTime)
    }

    override fun onEvent(event: Event) {
        if (event is LivingIncomingDamageEvent) {
            val victim = event.entity
            val damagedBody = event.source.extraData?.damagedBody
            val sourcePos = event.source.sourcePosition
            if (victim == holder) {

                fun block(body: PhysicsRigidBody) {
                    PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag().apply {
                        val contactPos = body.getPhysicsLocation(Vector3f()).apply { event.source.extraData?.manifoldId?.let { ManifoldPoints.getPositionWorldOnA(it, this) } }.toVec3()
                        putDouble("x", contactPos.x)
                        putDouble("y", contactPos.y)
                        putDouble("z", contactPos.z)
                    }))
                    onBlock.forEach { it.attach(this) }
                    event.isCanceled = true
                }

                bodies.forEach {
                    if (it.collideWithGroups != 0) {
                        if (damagedBody != null) {
                            if (it == damagedBody) {
                                block(it)
                            }
                        } else if (sourcePos != null) {
                            if (victim.canSee(sourcePos, 150.0)) {
                                block(it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun sync(data: CompoundTag, context: IPayloadContext) {
        val x = data.getDouble("x")
        val y = data.getDouble("y")
        val z = data.getDouble("z")
        blackBoard.write(SOFSkillContext.PARTICLE_POSITION, Vec3(x, y ,z))
        onBlock.forEach { it.attach(this) }
    }

    override fun onEnd() {
        blockAnim.cancel()
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<SuperArmorBlockSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                AnimIndex.CODEC.fieldOf("anim_index").forGetter { it.animIndex },
                Codec.INT.fieldOf("transition_time").forGetter { it.transitionTime },
                RigidBodyBinder.CODEC.listOf().fieldOf("bodies").forGetter { it.bodyBinders },
                Codec.STRING.listOf().optionalFieldOf("accessible_anims", listOf()).forGetter(SuperArmorBlockSkill::accessibleAnims),
                SkillComponent.CODEC.listOf().optionalFieldOf("on_skill_life_cycle", listOf()).forGetter { it.onAnimLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_block", listOf()).forGetter { it.onBlock },
            ).apply(it, ::SuperArmorBlockSkill)
        }
    }

}