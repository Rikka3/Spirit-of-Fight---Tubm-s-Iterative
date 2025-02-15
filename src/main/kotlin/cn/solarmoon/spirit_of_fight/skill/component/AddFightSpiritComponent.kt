package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.node.BehaviorNode
import cn.solarmoon.spark_core.skill.node.NodeStatus
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class AddFightSpiritComponent(
    val value: Int
): BehaviorNode() {

    override fun onStart(skill: SkillInstance) {
        val holder = skill.holder as? Entity ?: return
        if (!skill.level.isClientSide) {
            val fightSpirit = holder.getFightSpirit()
            fightSpirit.addStage(value)
            fightSpirit.syncToClient(holder.id)
        }
    }

    override fun onTick(skill: SkillInstance): NodeStatus {
        return NodeStatus.SUCCESS
    }

    override val codec: MapCodec<out BehaviorNode> = CODEC

    override fun copy(): BehaviorNode {
        return AddFightSpiritComponent(value)
    }

    companion object {
        val CODEC: MapCodec<AddFightSpiritComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.INT.fieldOf("value").forGetter { it.value }
            ).apply(it, ::AddFightSpiritComponent)
        }
    }

}