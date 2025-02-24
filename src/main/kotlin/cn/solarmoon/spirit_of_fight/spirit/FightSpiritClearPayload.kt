package cn.solarmoon.spirit_of_fight.spirit

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

object FightSpiritClearPayload: CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    @JvmStatic
    fun handleInServer(payload: FightSpiritClearPayload, context: IPayloadContext) {
        context.player().getFightSpirit().clear()
    }

    @JvmStatic
    val TYPE = CustomPacketPayload.Type<FightSpiritClearPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "fight_spirit_clear"))

    @JvmStatic
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, FightSpiritClearPayload> = StreamCodec.unit(FightSpiritClearPayload)

}