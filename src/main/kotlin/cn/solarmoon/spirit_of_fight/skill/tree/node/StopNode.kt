package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class StopNode(
    override val conditions: List<SkillTreeCondition>,
    override val children: List<SkillTreeNode> = listOf(),
    override val reserveTime: Int = 0,
    override val preInputId: String = "stop",
    override val preInputDuration: Int = 5,
) : SkillTreeNode {

    override val name: Component = Component.translatable("skill.stop.name")
    override val description: Component = Component.translatable("skill.stop.description")
    override val icon: ResourceLocation = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "textures/skill/stop.png")

    override fun onEntry(host: Player, level: Level, tree: SkillTree) {
        tree.currentSkill?.end()
    }

    override val codec: MapCodec<out SkillTreeNode> = CODEC

    companion object {
        val CODEC: MapCodec<StopNode> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                SkillTreeCondition.CODEC.listOf().fieldOf("conditions").forGetter { it.conditions },
                SkillTreeNode.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children },
                Codec.INT.optionalFieldOf("reserve_time", 0).forGetter { it.reserveTime },
                Codec.STRING.optionalFieldOf("pre_input_id", "stop").forGetter { it.preInputId },
                Codec.INT.optionalFieldOf("pre_input_duration", 5).forGetter { it.preInputDuration }
            ).apply(instance, ::StopNode)
        }
    }

}