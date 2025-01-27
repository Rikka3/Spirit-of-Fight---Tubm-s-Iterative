package cn.solarmoon.spirit_of_fight.hit

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.hit.type.HitType
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import io.netty.buffer.ByteBuf
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import net.neoforged.neoforge.network.handling.IPayloadContext

class HitAnimPayload(
    val entityId: Int,
    val hitTypeId: Int,
    val strength: AttackStrength,
    val boneName: String,
    val posSide: Side,
    val side: Side,
    val death: Boolean
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        @JvmStatic
        fun handleInClient(payload: HitAnimPayload, context: IPayloadContext) {
            val level = context.player().level()
            val entity = level.getEntity(payload.entityId) ?: return
            if (entity !is IEntityAnimatable<*>) return
            val hitType = SOFRegistries.HIT_TYPE.byId(payload.hitTypeId) ?: return
            val hitAnim = if (payload.death) hitType.getDeathAnimation(entity, payload.strength, payload.boneName, payload.posSide, payload.side)
            else hitType.getHitAnimation(entity, payload.strength, payload.boneName, payload.posSide, payload.side) ?: return
            entity.animController.setAnimation(hitAnim, 0)
        }

        @JvmStatic
        val TYPE = CustomPacketPayload.Type<HitAnimPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "hit_animation"))

        @JvmStatic
        val STREAM_CODEC = object : StreamCodec<FriendlyByteBuf, HitAnimPayload> {
            override fun decode(buffer: FriendlyByteBuf): HitAnimPayload {
                return HitAnimPayload(
                    buffer.readInt(),
                    buffer.readInt(),
                    AttackStrength.STREAM_CODEC.decode(buffer),
                    buffer.readUtf(),
                    Side.STREAM_CODEC.decode(buffer),
                    Side.STREAM_CODEC.decode(buffer),
                    buffer.readBoolean()
                )
            }

            override fun encode(buffer: FriendlyByteBuf, value: HitAnimPayload) {
                buffer.writeInt(value.entityId)
                buffer.writeInt(value.hitTypeId)
                AttackStrength.STREAM_CODEC.encode(buffer, value.strength)
                buffer.writeUtf(value.boneName)
                Side.STREAM_CODEC.encode(buffer, value.posSide)
                Side.STREAM_CODEC.encode(buffer, value.side)
                buffer.writeBoolean(value.death)
            }

        }
    }

}