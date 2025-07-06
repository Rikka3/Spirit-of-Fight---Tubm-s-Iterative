package cn.solarmoon.spirit_of_fight.particle.sync

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.particle.ParticlePresets
import cn.solarmoon.spirit_of_fight.sync.MoveDirectionPayload
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

class QuadraticParticlePayload(
    val amount: Int,
    val pos: Vec3,
    val particle: ParticleOptions
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        @JvmStatic
        fun handleInClient(payload: QuadraticParticlePayload, context: IPayloadContext) {
            val level = context.player().level()
            ParticlePresets(level, false).summonQuadraticParticle(payload.pos, payload.amount, payload.particle)
        }

        @JvmStatic
        val TYPE = CustomPacketPayload.Type<QuadraticParticlePayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "quadratic_particle"))

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, QuadraticParticlePayload::amount,
            SerializeHelper.VEC3_STREAM_CODEC, QuadraticParticlePayload::pos,
            ParticleTypes.STREAM_CODEC, QuadraticParticlePayload::particle,
            ::QuadraticParticlePayload
        )
    }

}