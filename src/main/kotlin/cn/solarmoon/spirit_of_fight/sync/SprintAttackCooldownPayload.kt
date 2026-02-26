package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class SprintAttackCooldownPayload(
    val entityId: Int,
    val cooldownUntil: Long
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<SprintAttackCooldownPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "sprint_attack_cooldown"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SprintAttackCooldownPayload> = StreamCodec.composite(
            ByteBufCodecs.INT, SprintAttackCooldownPayload::entityId,
            ByteBufCodecs.VAR_LONG, SprintAttackCooldownPayload::cooldownUntil,
            ::SprintAttackCooldownPayload
        )

        fun handleInClient(payload: SprintAttackCooldownPayload, context: IPayloadContext) {
            context.enqueueWork {
                val level = context.player().level()
                val entity = level.getEntity(payload.entityId)
                if (entity is IEntityPatch) {
                    entity.sprintAttackCooldownUntil = payload.cooldownUntil
                }
            }
        }
    }
}
