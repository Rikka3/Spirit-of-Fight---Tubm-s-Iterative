package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * Payload sent from client to server to set sprint attack cooldown.
 * Server sets the cooldown and syncs it back to all clients.
 */
object SprintAttackCooldownCostPayload: CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    @JvmStatic
    val TYPE = CustomPacketPayload.Type<SprintAttackCooldownCostPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "sprint_attack_cooldown_cost"))

    @JvmStatic
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SprintAttackCooldownCostPayload> = StreamCodec.unit(SprintAttackCooldownCostPayload)

    @JvmStatic
    fun handleInServer(payload: SprintAttackCooldownCostPayload, context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()
            if (player.isCreative) return@enqueueWork
            
            val patch = (player as Any) as? IEntityPatch
            val currentTime = player.level().gameTime
            
            if (patch != null) {
                // Set sprint attack cooldown (3 seconds = 60 ticks)
                patch.sprintAttackCooldownUntil = currentTime + 60
                SpiritOfFight.LOGGER.debug("SERVER: Sprint attack cooldown set for ${player.name.string} until $currentTime + 60")
            }
        }
    }
}
