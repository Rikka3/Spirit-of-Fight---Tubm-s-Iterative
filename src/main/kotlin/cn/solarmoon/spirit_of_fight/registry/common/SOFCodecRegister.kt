package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.AnimSkill
import cn.solarmoon.spirit_of_fight.skill.AttackSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.GuardSkill
import cn.solarmoon.spirit_of_fight.skill.SuperArmorBlockSkill
import cn.solarmoon.spirit_of_fight.skill.component.BodyFollowParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.ChargingParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.FightSpiritIncreaseComponent
import cn.solarmoon.spirit_of_fight.skill.component.FreezeAnimUntilHitComponent
import cn.solarmoon.spirit_of_fight.skill.component.FreezeAnimUntilOnGroundComponent
import cn.solarmoon.spirit_of_fight.skill.component.GaleParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.GrabComponent
import cn.solarmoon.spirit_of_fight.skill.component.SetGrabComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.QuadraticHitParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.UpdateSwimPoseComponent
import cn.solarmoon.spirit_of_fight.skill.tree.condition.AnyCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FallDistanceCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.HitTargetCondition
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
        event.register(SparkRegistries.SKILL_CODEC.key(), id("anim")) { AnimSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("attack")) { AttackSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("dodge")) { DodgeSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("guard")) { GuardSkill.CODEC }
        event.register(SparkRegistries.SKILL_CODEC.key(), id("super_armor_block")) { SuperArmorBlockSkill.CODEC }

        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("increase_fight_spirit")) { FightSpiritIncreaseComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("prevent_move_with_back")) { PreventMoveWithBackComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("charging_particle")) { ChargingParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("quadratic_hit_particle")) { QuadraticHitParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("gale_particle")) { GaleParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("body_follow_particle")) { BodyFollowParticleComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("set_grab")) { SetGrabComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("set_grab_remove")) { SetGrabComponent.Remove.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("grab")) { GrabComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("set_swimming")) { UpdateSwimPoseComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("freeze_anim_until_hit")) { FreezeAnimUntilHitComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("freeze_anim_until_on_ground")) { FreezeAnimUntilOnGroundComponent.CODEC }

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