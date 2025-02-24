package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.CommonAttackSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.GuardSkill
import cn.solarmoon.spirit_of_fight.skill.SuperArmorBlockSkill
import cn.solarmoon.spirit_of_fight.skill.component.BodyFollowParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.ChargingParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.FightSpiritIncreaseComponent
import cn.solarmoon.spirit_of_fight.skill.component.GaleParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.QuadraticParticleComponent
import cn.solarmoon.spirit_of_fight.skill.tree.condition.AnyCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.HitTargetCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
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
        event.register(SparkRegistries.SKILL_CODEC.key(), id("common_attack")) { CommonAttackSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("dodge")) { DodgeSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("guard")) { GuardSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("super_armor_block")) { SuperArmorBlockSkill.CODEC }

        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("increase_fight_spirit")) { FightSpiritIncreaseComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("prevent_move_with_back")) { PreventMoveWithBackComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("charging_particle")) { ChargingParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("quadratic_particle")) { QuadraticParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("gale_particle")) { GaleParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("body_follow_particle")) { BodyFollowParticleComponent.CODEC }

        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("key_input")) { KeyInputCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("hit_target")) { HitTargetCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("fight_spirit")) { FightSpiritCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("skill_is_ended")) { SkillEndCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("jumping")) { JumpingCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("sprinting")) { SprintingCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("reverse")) { ReverseCondition.CODEC }
        event.register(SOFRegistries.SKILL_TREE_CONDITION_CODEC.key(), id("any")) { AnyCondition.CODEC }

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