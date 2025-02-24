package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spark_core.skill.getSkillType
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

/**
 * @param reserveTime 每当条件匹配但还未能切换到下一个技能节点时会刷新一个预留时间，在此时间内技能树不会重置
 */
open class CommonNode(
    override val conditions: List<SkillTreeCondition>,
    var skillLocation: ResourceLocation,
    override val children: List<SkillTreeNode> = listOf(),
    override val reserveTime: Int = 0,
    override val preInputId: String = "combo",
    override val preInputDuration: Int = 5
): SkillTreeNode {

    override val name: Component = Component.translatable("skill.${skillLocation.namespace}.${skillLocation.path}.name")
    override val description: Component = Component.translatable("skill.${skillLocation.namespace}.${skillLocation.path}.description")
    override val icon: ResourceLocation = ResourceLocation.fromNamespaceAndPath(skillLocation.namespace, "textures/skill/${skillLocation.path}.png")

    override fun onEntry(host: Player, level: Level, tree: SkillTree) {
        tree.currentSkill = getSkillType(level, skillLocation).createSkill(host, level, true)
    }

    override val codec: MapCodec<out SkillTreeNode> = CODEC

    companion object {
        val CODEC: MapCodec<CommonNode> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                SkillTreeCondition.CODEC.listOf().fieldOf("conditions").forGetter { it.conditions },
                ResourceLocation.CODEC.fieldOf("skill").forGetter { it.skillLocation },
                SkillTreeNode.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children },
                Codec.INT.optionalFieldOf("reserve_time", 0).forGetter { it.reserveTime },
                Codec.STRING.optionalFieldOf("pre_input_id", "combo").forGetter { it.preInputId },
                Codec.INT.optionalFieldOf("pre_input_duration", 5).forGetter { it.preInputDuration }
            ).apply(instance, ::CommonNode)
        }
    }

}