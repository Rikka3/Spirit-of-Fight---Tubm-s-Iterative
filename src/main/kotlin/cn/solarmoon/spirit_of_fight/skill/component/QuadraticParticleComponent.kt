package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.registry.common.SOFSkillContext
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import kotlin.random.Random

class QuadraticParticleComponent(
    val particleType: ParticleOptions,
    val amount: Int = 15
): SkillComponent() {

    override fun onAttach() {
        val level = skill.level
        if (level.isClientSide) {
            val pos = skill.blackBoard.require(SOFSkillContext.PARTICLE_POSITION, this)
            repeat(amount) {
                level.addParticle(particleType, pos.x, pos.y, pos.z, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5)
            }
        }
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<QuadraticParticleComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                ParticleTypes.CODEC.fieldOf("particle").forGetter { it.particleType },
                Codec.INT.optionalFieldOf("amount", 15).forGetter { it.amount }
            ).apply(it, ::QuadraticParticleComponent)
        }
    }

}