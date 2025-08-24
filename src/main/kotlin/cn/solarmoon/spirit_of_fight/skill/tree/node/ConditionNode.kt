package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

class ConditionNode(
    override val conditions: List<SkillTreeCondition>,
    override val children: List<SkillTreeNode> = listOf(),
    override val reserveTime: Int = 0
): SkillTreeNode {

    override val name: MutableComponent? = null
    override val description: MutableComponent? = null
    override val icon = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/skill/tree/common/condition_node.png")
    override val persistentCondition: Boolean = true

    override val codec: MapCodec<out SkillTreeNode> = CODEC

    companion object {
        val CODEC: MapCodec<ConditionNode> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                SkillTreeCondition.CODEC.listOf().fieldOf("conditions").forGetter { it.conditions },
                SkillTreeNode.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children },
                Codec.INT.optionalFieldOf("reserve_time", 0).forGetter { it.reserveTime }
            ).apply(instance, ::ConditionNode)
        }
    }

}