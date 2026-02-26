package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.math.max

object JumpAttackCostPayload: CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    @JvmStatic
    val TYPE = CustomPacketPayload.Type<JumpAttackCostPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "jump_attack_cost"))

    @JvmStatic
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, JumpAttackCostPayload> = StreamCodec.unit(JumpAttackCostPayload)

    @JvmStatic
    fun handleInServer(payload: JumpAttackCostPayload, context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()
            if (player.isCreative) return@enqueueWork
            
            val patch = (player as Any) as? IEntityPatch
            val currentTime = player.level().gameTime
            
            if (patch != null) {
                // Authoritative server-side check
                if (currentTime - patch.lastJumpAttackTick >= 20 && player.foodData.foodLevel >= 2) {
                    val oldFood = player.foodData.foodLevel
                    player.foodData.foodLevel = max(0, oldFood - 2)
                    player.foodData.addExhaustion(0.1f)
                    patch.lastJumpAttackTick = currentTime
                    
                    SpiritOfFight.LOGGER.info("SERVER: Jump attack cost applied for ${player.name.string}. $oldFood -> ${player.foodData.foodLevel}")
                    
                    // Sync cooldown back to client
                    PacketDistributor.sendToPlayer(player as net.minecraft.server.level.ServerPlayer, JumpAttackCooldownPayload(player.id, currentTime))
                } else {
                    // Reject if cooldown/hunger not met (e.g. hack/lag)
                    SpiritOfFight.LOGGER.warn("SERVER: Jump attack cost REJECTED for ${player.name.string}. Cooldown/Hunger not met.")
                }
            }
        }
    }
}
