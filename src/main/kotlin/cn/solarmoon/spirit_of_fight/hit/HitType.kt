package cn.solarmoon.spirit_of_fight.hit

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class HitType(
    val name: String,
    val level: HitLevel = HitLevel.WHEN_NOT_PLAYING_SKILLS
) {

    companion object {
        val CODEC: Codec<HitType> = RecordCodecBuilder.create {
            it.group(
                Codec.STRING.fieldOf("name").forGetter { it.name },
                HitLevel.CODEC.optionalFieldOf("level", HitLevel.WHEN_NOT_PLAYING_SKILLS).forGetter { it.level }
            ).apply(it, ::HitType)
        }
    }

}