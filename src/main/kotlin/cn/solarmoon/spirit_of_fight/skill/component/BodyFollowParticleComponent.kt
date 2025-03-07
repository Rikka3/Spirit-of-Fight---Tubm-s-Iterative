package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.physics.collision.PhysicsCollisionObjectTicker
import cn.solarmoon.spark_core.physics.toVec3
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.level.Level

class BodyFollowParticleComponent(
    val particle: ParticleOptions
): SkillComponent() {

    override fun onAttach(): Boolean {
        skill.physicsBodies.forEach {
            it.addPhysicsTicker(object : PhysicsCollisionObjectTicker{
                override fun mcTick(
                    body: PhysicsCollisionObject,
                    level: Level
                ) {
                    if (body.collideWithGroups == 0) return
                    val pos = body.getPhysicsLocation(Vector3f()).toVec3()
                    val speed = (if (body is PhysicsRigidBody) body.getLinearVelocity(Vector3f()) else Vector3f()).toVec3()
                    level.addParticle(particle, pos.x, pos.y, pos.z, speed.x, speed.y, speed.z)
                }
            })
        }
        return true
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<BodyFollowParticleComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                ParticleTypes.CODEC.fieldOf("particle").forGetter { it.particle }
            ).apply(it, ::BodyFollowParticleComponent)
        }
    }

}