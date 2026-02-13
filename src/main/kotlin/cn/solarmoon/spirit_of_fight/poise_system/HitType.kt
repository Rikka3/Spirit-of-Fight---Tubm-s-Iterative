package cn.solarmoon.spirit_of_fight.poise_system

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class HitType(
    val name: String,
    val poiseDamage: Int,
    val fsDamage: Int
) {

    companion object {
        val CODEC: Codec<HitType> = RecordCodecBuilder.create {
            it.group(
                Codec.STRING.fieldOf("name").forGetter { it.name },
                Codec.INT.fieldOf("poise_damage").forGetter { it.poiseDamage },
                Codec.INT.fieldOf("fight_spirit_damage").forGetter { it.fsDamage }
            ).apply(it, ::HitType)
        }
    }

}