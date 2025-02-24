package cn.solarmoon.spirit_of_fight.sync

import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class MoveDirectionPayload(
    val sideId: Int
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        @JvmStatic
        fun handle(payload: MoveDirectionPayload, context: IPayloadContext) {
            val player = context.player()
            player.moveDirection = MoveDirection.entries.getOrNull(payload.sideId)
        }

        @JvmStatic
        val TYPE = CustomPacketPayload.Type<MoveDirectionPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "move_direction"))

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MoveDirectionPayload::sideId,
            ::MoveDirectionPayload
        )
    }

}