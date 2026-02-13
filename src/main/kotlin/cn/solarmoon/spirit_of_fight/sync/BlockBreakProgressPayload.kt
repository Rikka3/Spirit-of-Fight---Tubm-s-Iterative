package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.BlockBreakingData
import cn.solarmoon.spirit_of_fight.skill.getBlockBreakingData
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * Network payload for syncing block break progress to client
 */
data class BlockBreakProgressPayload(
    val entityId: Int,
    val damage: Float,
    val maxDamage: Float,
    val isBreaking: Boolean
) : CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        @JvmStatic
        fun handleInClient(payload: BlockBreakProgressPayload, context: IPayloadContext) {
            val level = context.player().level()
            val entity = level.getEntity(payload.entityId) ?: return
            val blockBreakingData = entity.getBlockBreakingData()
            blockBreakingData.damage = payload.damage
            blockBreakingData.maxDamage = payload.maxDamage
            blockBreakingData.isBreaking = payload.isBreaking
        }

        @JvmStatic
        val TYPE = CustomPacketPayload.Type<BlockBreakProgressPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "block_break_progress"))

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BlockBreakProgressPayload::entityId,
            ByteBufCodecs.FLOAT, BlockBreakProgressPayload::damage,
            ByteBufCodecs.FLOAT, BlockBreakProgressPayload::maxDamage,
            ByteBufCodecs.BOOL, BlockBreakProgressPayload::isBreaking,
            ::BlockBreakProgressPayload
        )

        /**
         * Create from BlockBreakingData
         */
        fun fromData(entityId: Int, data: BlockBreakingData): BlockBreakProgressPayload {
            return BlockBreakProgressPayload(
                entityId,
                data.damage,
                data.maxDamage,
                data.isBreaking
            )
        }
    }
}
