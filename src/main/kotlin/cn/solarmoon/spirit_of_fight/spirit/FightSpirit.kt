package cn.solarmoon.spirit_of_fight.spirit

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.network.PacketDistributor

class FightSpirit(
    var value: Int = 0,
    var maxValue: Int = 100,
    var fadeTick: Int = 0,
    var maxTickToFade: Int = 300
) {

    val shouldFade get() = value > 0 && fadeTick >= maxTickToFade
    val isFull get() = value >= maxValue
    val isEmpty get() = value <= 0

    fun refresh(spirit: FightSpirit) {
        value = spirit.value
        maxValue = spirit.maxValue
        fadeTick = spirit.fadeTick
        maxTickToFade = spirit.maxTickToFade
    }

    fun addStage(amount: Int) {
        fadeTick = 0
        if (value < maxValue) {
            value = (value + amount).coerceIn(0, maxValue)
        }
    }

    fun removeStage(amount: Int) {
        fadeTick = 0
        if (value > 0) {
            value = (value - amount).coerceIn(0, maxValue)
        }
    }

    fun clear() {
        value = 0
        fadeTick = 0
    }

    fun tick() {
        if (value > 0) {
            if (shouldFade) {
                value--
            }
            else fadeTick++
        }
    }

    fun syncToClient(entityId: Int) {
        PacketDistributor.sendToAllPlayers(FightSpiritPayload(entityId, this))
    }

    companion object {
        @JvmStatic
        val CODEC: Codec<FightSpirit> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("value").forGetter { it.value },
                Codec.INT.fieldOf("max_value").forGetter { it.maxValue },
                Codec.INT.fieldOf("tick").forGetter { it.fadeTick },
                Codec.INT.fieldOf("max_tick").forGetter { it.maxTickToFade }
            ).apply(it, ::FightSpirit)
        }

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FightSpirit::value,
            ByteBufCodecs.INT, FightSpirit::maxValue,
            ByteBufCodecs.INT, FightSpirit::fadeTick,
            ByteBufCodecs.INT, FightSpirit::maxTickToFade,
            ::FightSpirit
        )
    }

}