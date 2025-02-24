package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import kotlin.math.cos
import kotlin.math.sin

class ChargingParticleComponent(
    val particleType: ParticleOptions = ParticleTypes.WHITE_SMOKE
): SkillComponent() {

    override fun onAttach() {
        onTick()
    }

    override fun onTick() {
        val entity = skill.holder as? Entity ?: return
        val level = skill.level
        val fs = entity.getFightSpirit()
        val particleCount = 30
        val radius = 2.5
        if (skill.runTime % 5 == 0) {
            repeat(particleCount) {
                val angle = (it * 2 * Math.PI / particleCount)
                val center = entity.position()
                val particleX = center.x + radius * cos(angle)
                val particleY = center.y + 0.025
                val particleZ = center.z + radius * sin(angle)
                val speedX = if (fs.isFull) (center.x - particleX) * 0.1 else 0.0
                val speedY = 0.0
                val speedZ = if (fs.isFull) (center.z - particleZ) * 0.1 else 0.0
                level.addParticle(particleType, particleX, particleY, particleZ, speedX, speedY, speedZ)
            }
        }
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<ChargingParticleComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                ParticleTypes.CODEC.optionalFieldOf("particle", ParticleTypes.WHITE_SMOKE).forGetter { it.particleType }
            ).apply(it, ::ChargingParticleComponent)
        }
    }

}