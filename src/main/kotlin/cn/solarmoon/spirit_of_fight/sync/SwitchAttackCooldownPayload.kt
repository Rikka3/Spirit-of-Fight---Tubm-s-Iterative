package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class SwitchAttackCooldownPayload(
    val entityId: Int,
    val cooldownUntil: Long
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<SwitchAttackCooldownPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "switch_attack_cooldown"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SwitchAttackCooldownPayload> = StreamCodec.composite(
            ByteBufCodecs.INT, SwitchAttackCooldownPayload::entityId,
            ByteBufCodecs.VAR_LONG, SwitchAttackCooldownPayload::cooldownUntil,
            ::SwitchAttackCooldownPayload
        )

        fun handleInClient(payload: SwitchAttackCooldownPayload, context: IPayloadContext) {
            context.enqueueWork {
                val level = context.player().level()
                val entity = level.getEntity(payload.entityId)
                if (entity is IEntityPatch) {
                    entity.switchAttackCooldownUntil = payload.cooldownUntil
                }
            }
        }
    }
}
