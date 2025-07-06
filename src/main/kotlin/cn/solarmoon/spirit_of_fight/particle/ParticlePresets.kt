package cn.solarmoon.spirit_of_fight.particle

import cn.solarmoon.spark_core.entity.getRelativeVector
import cn.solarmoon.spirit_of_fight.particle.sync.QuadraticParticlePayload
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ParticlePresets(
    val level: Level,
    val sync: Boolean
) {

    fun summonQuadraticParticle(pos: Vec3, amount: Int, particle: ParticleOptions) {
        if (sync) {
            PacketDistributor.sendToAllPlayers(QuadraticParticlePayload(amount, pos, particle))
        } else {
            repeat(amount) {
                level.addParticle(particle, pos.x, pos.y, pos.z, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5)
            }
        }
    }

    fun summonGaleParticle(entity: Entity, amount: Int, speed: Double, offset: Vec3, particleType: ParticleOptions) {
        repeat(amount) {
            val offset = entity.getRelativeVector(offset)
            val angle = (it * 2 * Math.PI / amount)
            val center = entity.position()
            val particleX = center.x + offset.x
            val particleY = center.y + 0.05 + offset.y
            val particleZ = center.z + offset.z
            val speedX = -speed * cos(angle)
            val speedY = 0.025
            val speedZ = -speed * sin(angle)
            level.addParticle(particleType, particleX, particleY, particleZ, speedX, speedY, speedZ)
        }
    }

    fun summonChargingParticle(center: Vec3, amount: Int, radius: Double, still: Boolean, particleType: ParticleOptions) {
        repeat(amount) {
            val angle = (it * 2 * Math.PI / amount)
            val particleX = center.x + radius * cos(angle)
            val particleY = center.y + 0.025
            val particleZ = center.z + radius * sin(angle)
            val speedX = if (!still) (center.x - particleX) * 0.1 else 0.0
            val speedY = 0.0
            val speedZ = if (!still) (center.z - particleZ) * 0.1 else 0.0
            level.addParticle(particleType, particleX, particleY, particleZ, speedX, speedY, speedZ)
        }
    }

}