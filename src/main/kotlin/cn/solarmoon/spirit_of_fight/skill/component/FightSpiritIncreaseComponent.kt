package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class FightSpiritIncreaseComponent(
    val value: Int = 40
): SkillComponent() {

    override fun onAttach(): Boolean {
        val entity = skill.holder as? Entity ?: return false
        val level = entity.level()
        if (!level.isClientSide) {
            val mul = 1.0
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.addStage((value * mul).toInt())
            fightSpirit.syncToClient(entity.id)
        }
        return true
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<FightSpiritIncreaseComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.INT.fieldOf("value").forGetter { it.value }
            ).apply(it, ::FightSpiritIncreaseComponent)
        }
    }

}