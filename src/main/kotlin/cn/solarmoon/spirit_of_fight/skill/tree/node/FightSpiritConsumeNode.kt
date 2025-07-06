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
    preInputId: String,
    children: List<SkillTreeNode> = listOf(),
    reserveTime: Int = 2,
    preInputDuration: Int = 5
): CommonNode(condition, skillLocation, preInputId, children, reserveTime, preInputDuration) {

    override fun onEntry(host: Player, level: Level, tree: SkillTree): Boolean {
        host.getFightSpirit().clear()
        PacketDistributor.sendToServer(FightSpiritClearPayload)
        super.onEntry(host, level, tree)
        return true
    }

    override val codec: MapCodec<out SkillTreeNode> = CODEC

    companion object {
        val CODEC: MapCodec<FightSpiritConsumeNode> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                SkillTreeCondition.CODEC.listOf().fieldOf("conditions").forGetter { it.conditions },
                ResourceLocation.CODEC.fieldOf("skill").forGetter { it.skillLocation },
                Codec.STRING.fieldOf("pre_input_id").forGetter { it.preInputId },
                SkillTreeNode.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children },
                Codec.INT.optionalFieldOf("reserve_time", 2).forGetter { it.reserveTime },
                Codec.INT.optionalFieldOf("pre_input_duration", 5).forGetter { it.preInputDuration }
            ).apply(instance, ::FightSpiritConsumeNode)
        }
    }

}