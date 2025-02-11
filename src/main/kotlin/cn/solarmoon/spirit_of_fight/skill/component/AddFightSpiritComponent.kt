package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.attachment.IAttachmentHolder

class AddFightSpiritComponent(
    val value: Int
): SkillComponent() {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return AddFightSpiritComponent(value)
    }

    override fun onActive() {
        val holder = skill.holder as? Entity ?: return
        if (!skill.level.isClientSide) {
            val fightSpirit = holder.getFightSpirit()
            fightSpirit.addStage(value)
            fightSpirit.syncToClient(holder.id)
        }
    }

    override fun onUpdate() {}

    override fun onEnd() {}

    companion object {
        val CODEC: MapCodec<AddFightSpiritComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.INT.fieldOf("value").forGetter { it.value }
            ).apply(it, ::AddFightSpiritComponent)
        }
    }

}