package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiCombo0
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiCombo1
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiCombo2
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiGuard
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiJumpAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiSquatDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.滑铲
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.白眉战技
import cn.solarmoon.spirit_of_fight.skill.controller.component.BaimeiSquatDodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ChangeableComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.DodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.GuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.HoldReleaseSkillControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.JumpAttackControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3

class BaimeiFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("baimei", holder, animatable, 2.0, true) {

    override val boxLength: DVector3 = DVector3(1.0, 1.0, 1.0)
    override val boxOffset: DVector3 = DVector3(0.0, 0.0, 0.0)

    val combo0 = addSkill(BaimeiCombo0(animatable))
    val combo1 = addSkill(BaimeiCombo1(animatable))
    val combo2 = addSkill(BaimeiCombo2(animatable))
    val sprintingAttack = addSkill(滑铲(animatable))
    val jumpAttack = addSkill(BaimeiJumpAttack(animatable))
    val guard = addSkill(BaimeiGuard(animatable))
    val dodge = addSkill(BaimeiDodge(animatable))
    val dodgeShift = addSkill(BaimeiSquatDodge(animatable))
    val special = addSkill(白眉战技(animatable))

    init {
        addComponent(HoldReleaseSkillControlComponent(special))
        addComponent(JumpAttackControlComponent(jumpAttack))
        addComponent(SprintAttackControlComponent(sprintingAttack))
        addComponent(ChangeableComboControlComponent(combo0, combo1, combo2))
        addComponent(BaimeiSquatDodgeControlComponent(dodgeShift))
        addComponent(DodgeControlComponent(dodge))
        addComponent(GuardControlComponent(guard))
    }

    override fun isAvailable(): Boolean {
        return holder.mainHandItem.`is`(SOFItemTags.FORGE_GLOVES) && holder.offhandItem.`is`(SOFItemTags.FORGE_GLOVES)
    }

}