package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.physics.toVec3
import cn.solarmoon.spark_core.skill.Skill
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
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.random.Random

class SuperArmorBlockSkill(
    val animPlayer: PlayAnimationComponent,
    val bodyBinders: List<RigidBodyBinder>,
    val accessibleAnims: List<String> = listOf(),
    val onAnimLifeCycle: List<SkillComponent> = listOf(),
    val onBlock: List<SkillComponent> = listOf(),
    val blockParticle: ParticleOptions = ParticleTypes.CRIT
): Skill() {

    override fun onActive(): Boolean {
        bodyBinders.forEach { it.attach(this) }
        physicsBodies.forEach { it.flags.add("guard") }

        animPlayer.attach(this)
        animPlayer.anim.apply {
            shouldTurnBody = true

            rejectNewAnim = { next ->
                next?.name !in accessibleAnims
            }

            onEvent<AnimEvent.End> {
                end()
            }
        }

        return true
    }

    override fun onHurt(event: LivingIncomingDamageEvent) {
        val victim = event.entity
        val damagedBody = event.source.extraData?.damagedBody
        val sourcePos = event.source.sourcePosition
        if (victim == holder) {

            fun block(body: PhysicsCollisionObject) {
                PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag().apply {
                    val contactPos = body.getPhysicsLocation(Vector3f()).apply { event.source.extraData?.manifoldId?.let { ManifoldPoints.getPositionWorldOnA(it, this) } }.toVec3()
                    putDouble("x", contactPos.x)
                    putDouble("y", contactPos.y)
                    putDouble("z", contactPos.z)
                }))
                onBlock.forEach { it.attach(this) }
                event.isCanceled = true
            }

            physicsBodies.forEach {
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

    override fun onSync(data: CompoundTag, context: IPayloadContext) {
        val x = data.getDouble("x")
        val y = data.getDouble("y")
        val z = data.getDouble("z")
        repeat(15) {
            level.addParticle(blockParticle, x, y, z, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5)
        }
        onBlock.forEach { it.attach(this) }
    }

    override fun onEnd() {
        animPlayer.anim.cancel()
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<SuperArmorBlockSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                PlayAnimationComponent.CODEC.fieldOf("animation").forGetter { it.animPlayer },
                RigidBodyBinder.CODEC.listOf().fieldOf("bodies").forGetter { it.bodyBinders },
                Codec.STRING.listOf().optionalFieldOf("accessible_anims", listOf()).forGetter(SuperArmorBlockSkill::accessibleAnims),
                SkillComponent.CODEC.listOf().optionalFieldOf("on_skill_life_cycle", listOf()).forGetter { it.onAnimLifeCycle },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_block", listOf()).forGetter { it.onBlock },
                ParticleTypes.CODEC.optionalFieldOf("block_particle", ParticleTypes.CRIT).forGetter { it.blockParticle },
            ).apply(it, ::SuperArmorBlockSkill)
        }
    }

}