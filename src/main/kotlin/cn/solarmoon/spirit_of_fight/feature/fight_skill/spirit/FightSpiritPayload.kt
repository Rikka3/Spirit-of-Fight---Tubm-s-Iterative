package cn.solarmoon.spirit_of_fight.feature.fight_skill.spirit

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

data class FightSpiritPayload(
    val entityId: Int,
    val spirit: FightSpirit
): CustomPacketPayload {

    enum class Type(val id: Int) {
        ADD(0), SYNC(1);

        companion object {
            @JvmStatic
            fun getById(id: Int) = Type.entries.first { it.id == id }
        }
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        @JvmStatic
        fun handleInClient(payload: FightSpiritPayload, context: IPayloadContext) {
            val level = context.player().level()
            val entity = level.getEntity(payload.entityId) ?: return
            entity.getFightSpirit().reset(payload.spirit)
        }

        @JvmStatic
        val TYPE = CustomPacketPayload.Type<FightSpiritPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "client_fight_spirit"))

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FightSpiritPayload::entityId,
            FightSpirit.STREAM_CODEC, FightSpiritPayload::spirit,
            ::FightSpiritPayload
        )
    }

}
