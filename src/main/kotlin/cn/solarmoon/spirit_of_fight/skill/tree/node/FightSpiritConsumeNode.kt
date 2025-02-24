package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritClearPayload
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.PacketDistributor

class FightSpiritConsumeNode(
    condition: List<SkillTreeCondition>,
    skillLocation: ResourceLocation,
    children: List<SkillTreeNode> = listOf(),
    reserveTime: Int = 0,
    preInputId: String = "combo",
    preInputDuration: Int = 5
): CommonNode(condition, skillLocation, children, reserveTime, preInputId, preInputDuration) {

    override fun onEntry(host: Player, level: Level, tree: SkillTree) {
        host.getFightSpirit().clear()
        PacketDistributor.sendToServer(FightSpiritClearPayload)
        super.onEntry(host, level, tree)
    }

    override val codec: MapCodec<out SkillTreeNode> = CODEC

    companion object {
        val CODEC: MapCodec<FightSpiritConsumeNode> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                SkillTreeCondition.CODEC.listOf().fieldOf("conditions").forGetter { it.conditions },
                ResourceLocation.CODEC.fieldOf("skill").forGetter { it.skillLocation },
                SkillTreeNode.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children },
                Codec.INT.optionalFieldOf("reserve_time", 0).forGetter { it.reserveTime },
                Codec.STRING.optionalFieldOf("pre_input_id", "combo").forGetter { it.preInputId },
                Codec.INT.optionalFieldOf("pre_input_duration", 5).forGetter { it.preInputDuration }
            ).apply(instance, ::FightSpiritConsumeNode)
        }
    }

}