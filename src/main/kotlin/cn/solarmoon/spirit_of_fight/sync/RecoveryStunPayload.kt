package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class RecoveryStunPayload(
    val entityId: Int,
    val isRecoveryStun: Boolean
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<RecoveryStunPayload> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<RecoveryStunPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "recovery_stun"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RecoveryStunPayload> = StreamCodec.composite(
            ByteBufCodecs.INT, RecoveryStunPayload::entityId,
            ByteBufCodecs.BOOL, RecoveryStunPayload::isRecoveryStun,
            ::RecoveryStunPayload
        )

        fun handleInClient(payload: RecoveryStunPayload, context: IPayloadContext) {
            context.enqueueWork {
                val level = context.player().level()
                val entity = level.getEntity(payload.entityId) ?: return@enqueueWork
                val patch = (entity as Any) as? IEntityPatch
                if (patch != null) {
                    patch.isRecoveryStun = payload.isRecoveryStun
                }
            }
        }
    }
}
