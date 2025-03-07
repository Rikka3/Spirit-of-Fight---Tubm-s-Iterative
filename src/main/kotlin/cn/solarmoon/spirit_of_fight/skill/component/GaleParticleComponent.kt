package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.entity.getRelativeVector
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

class GaleParticleComponent(
    val particleType: ParticleOptions = ParticleTypes.CLOUD,
    val speedFactor: Double = 1.0,
    val offset: Vec3 = Vec3.ZERO
): SkillComponent() {

    override fun onAttach(): Boolean {
        val entity = skill.holder as? Entity ?: return false
        val level = skill.level
        val particleCount = 30
        repeat(particleCount) {
            val offset = entity.getRelativeVector(offset)
            val angle = (it * 2 * Math.PI / particleCount)
            val center = entity.position()
            val particleX = center.x + offset.x
            val particleY = center.y + 0.05 + offset.y
            val particleZ = center.z + offset.z
            val speedX = -speedFactor * cos(angle)
            val speedY = 0.025
            val speedZ = -speedFactor * sin(angle)
            level.addParticle(particleType, particleX, particleY, particleZ, speedX, speedY, speedZ)
        }
        return true
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<GaleParticleComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                ParticleTypes.CODEC.optionalFieldOf("particle", ParticleTypes.CLOUD).forGetter { it.particleType },
                Codec.DOUBLE.optionalFieldOf("speed_factor", 1.0).forGetter { it.speedFactor },
                Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter { it.offset },
            ).apply(it, ::GaleParticleComponent)
        }
    }

}