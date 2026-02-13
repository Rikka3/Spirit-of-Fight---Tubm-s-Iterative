package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

data class ComboCooldownPayload(
    val entityId: Int,
    val comboCooldownUntil: Long
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<ComboCooldownPayload> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<ComboCooldownPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "combo_cooldown"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ComboCooldownPayload> = StreamCodec.composite(
            ByteBufCodecs.INT, ComboCooldownPayload::entityId,
            ByteBufCodecs.VAR_LONG, ComboCooldownPayload::comboCooldownUntil,
            ::ComboCooldownPayload
        )

        fun handleInClient(payload: ComboCooldownPayload, context: IPayloadContext) {
            context.enqueueWork {
                val level = context.player().level()
                val entity = level.getEntity(payload.entityId) ?: return@enqueueWork
                val patch = (entity as Any) as? IEntityPatch
                if (patch != null) {
                    patch.comboCooldownUntil = payload.comboCooldownUntil
                }
            }
        }
    }
}
