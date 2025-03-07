package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.payload.SkillComponentPayload
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.math.Vector3f
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.random.Random

class QuadraticHitParticleComponent(
    val particleType: ParticleOptions,
    val actualHit: Boolean = true,
    val amount: Int = 15
): SkillComponent() {

    override fun onTargetDamage(event: LivingDamageEvent) {
        if (event is LivingDamageEvent.Post && actualHit) {
            val pos = event.source.extraData?.manifoldId?.let { Vector3f().apply { ManifoldPoints.getPositionWorldOnA(it, this) } } ?: return
            PacketDistributor.sendToAllPlayers(SkillComponentPayload(this, CompoundTag().apply {
                putFloat("x", pos.x)
                putFloat("y", pos.y)
                putFloat("z", pos.z)
            }))
        }
    }

    override fun onTargetHurt(event: LivingIncomingDamageEvent) {
        if (actualHit) return
        val pos = event.source.extraData?.manifoldId?.let { Vector3f().apply { ManifoldPoints.getPositionWorldOnA(it, this) } } ?: return
        PacketDistributor.sendToAllPlayers(SkillComponentPayload(this, CompoundTag().apply {
            putFloat("x", pos.x)
            putFloat("y", pos.y)
            putFloat("z", pos.z)
        }))
    }

    override fun sync(data: CompoundTag, context: IPayloadContext) {
        val x = data.getFloat("x").toDouble()
        val y = data.getFloat("y").toDouble()
        val z = data.getFloat("z").toDouble()
        repeat(amount) {
            skill.level.addParticle(particleType, x, y, z, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5)
        }
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<QuadraticHitParticleComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                ParticleTypes.CODEC.fieldOf("particle").forGetter { it.particleType },
                Codec.BOOL.optionalFieldOf("actual_hit", true).forGetter { it.actualHit },
                Codec.INT.optionalFieldOf("amount", 15).forGetter { it.amount }
            ).apply(it, ::QuadraticHitParticleComponent)
        }
    }

}