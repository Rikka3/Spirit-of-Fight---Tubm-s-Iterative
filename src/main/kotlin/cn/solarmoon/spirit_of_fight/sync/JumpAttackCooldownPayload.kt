package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class JumpAttackCooldownPayload(
    val entityId: Int,
    val lastJumpAttackTick: Long
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<JumpAttackCooldownPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "jump_attack_cooldown"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, JumpAttackCooldownPayload> = StreamCodec.composite(
            ByteBufCodecs.INT, JumpAttackCooldownPayload::entityId,
            ByteBufCodecs.VAR_LONG, JumpAttackCooldownPayload::lastJumpAttackTick,
            ::JumpAttackCooldownPayload
        )

        fun handleInClient(payload: JumpAttackCooldownPayload, context: IPayloadContext) {
            context.enqueueWork {
                val level = context.player().level()
                val entity = level.getEntity(payload.entityId)
                if (entity is IEntityPatch) {
                    entity.lastJumpAttackTick = payload.lastJumpAttackTick
                }
            }
        }
    }
}
