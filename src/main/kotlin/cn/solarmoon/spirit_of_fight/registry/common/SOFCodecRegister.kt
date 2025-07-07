package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.tree.condition.AnyCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FallDistanceCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.HitTargetCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.HoldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OffHandCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillEndCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.FightSpiritConsumeNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.RegisterEvent

object SOFCodecRegister {

    private fun reg(event: RegisterEvent) {
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("key_input")) { KeyInputCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("hit_target")) { HitTargetCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("fight_spirit")) { FightSpiritCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("fall_distance")) { FallDistanceCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("on_ground")) { OnGroundCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("skill_ended")) { SkillEndCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("jumping")) { JumpingCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("sprinting")) { SprintingCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("off_hand")) { OffHandCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("reverse")) { ReverseCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("any")) { AnyCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("wield_style")) { HoldCondition.CODEC }

        event.register(SOFRegistries.SKILL_TREE_NODE_CODEC.key(), id("node")) { CommonNode.CODEC }
        event.register(SOFRegistries.SKILL_TREE_NODE_CODEC.key(), id("fight_spirit_consumer")) { FightSpiritConsumeNode.CODEC }
        event.register(SOFRegistries.SKILL_TREE_NODE_CODEC.key(), id("stop")) { StopNode.CODEC }
    }

    private fun id(id: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id)

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}