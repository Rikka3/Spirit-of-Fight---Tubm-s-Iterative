package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class SetGrabComponent(
    val maxAmount: Int = 1
): SkillComponent() {

    override fun onAttach(): Boolean {
        val entity = skill.holder as? Entity ?: return false
        entity.grabManager.addAll(skill.getTargets().take(maxAmount))
        return true
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<SetGrabComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.INT.optionalFieldOf("max_amount", 1).forGetter { it.maxAmount }
            ).apply(it, ::SetGrabComponent)
        }
    }

    class Remove: SkillComponent() {

        override fun onAttach(): Boolean {
            val entity = skill.holder as? Entity ?: return false
            entity.grabManager.clear()
            return true
        }

        override val codec: MapCodec<out SkillComponent> = CODEC

        companion object {
            val CODEC: MapCodec<Remove> = RecordCodecBuilder.mapCodec {
                it.group(Codec.EMPTY.forGetter { Unit.INSTANCE }).apply(it) { Remove() }
            }
        }

    }

}