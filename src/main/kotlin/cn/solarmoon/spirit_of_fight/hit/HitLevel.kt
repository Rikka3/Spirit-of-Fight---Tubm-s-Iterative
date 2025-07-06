package cn.solarmoon.spirit_of_fight.hit

import com.mojang.serialization.Codec

enum class HitLevel {
    WHEN_NOT_PLAYING_SKILLS, ANYWAY;

    companion object {
        val CODEC: Codec<HitLevel> = Codec.STRING.xmap(
            { HitLevel.valueOf(it.uppercase()) },
            { it.toString().lowercase() }
        )
    }
}