package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class DodgeCooldownPayload(
    val entityId: Int,
    val lastDodgeTick: Long
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<DodgeCooldownPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "dodge_cooldown"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, DodgeCooldownPayload> = StreamCodec.composite(
            ByteBufCodecs.INT, DodgeCooldownPayload::entityId,
            ByteBufCodecs.VAR_LONG, DodgeCooldownPayload::lastDodgeTick,
            ::DodgeCooldownPayload
        )

        fun handleInClient(payload: DodgeCooldownPayload, context: IPayloadContext) {
            context.enqueueWork {
                val level = context.player().level()
                val entity = level.getEntity(payload.entityId)
                if (entity is IEntityPatch) {
                    entity.lastDodgeTick = payload.lastDodgeTick
                }
            }
        }
    }
}
