package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC1
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC2
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldGuardSkill
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordCombo0
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordCombo1
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordCombo2
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordGuard
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordJumpAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.SwordSprintingAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.sword.剑战技
import cn.solarmoon.spirit_of_fight.skill.controller.component.ChangeableComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.DodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.GuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.HoldReleaseSkillControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.JumpAttackControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ShieldComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ShieldGuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3

class SwordFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("sword", holder, animatable, 1.6, false) {

    override val boxLength: DVector3 = DVector3(0.65, 0.65, 1.15)
    override val boxOffset: DVector3 = DVector3(0.0, 0.0, -0.575)

    val combo0 = addSkill(SwordCombo0(animatable))
    val combo1 = addSkill(SwordCombo1(animatable))
    val combo2 = addSkill(SwordCombo2(animatable))
    val sprintingAttack = addSkill(SwordSprintingAttack(animatable))
    val jumpAttack = addSkill(SwordJumpAttack(animatable))
    val guard = addSkill(SwordGuard(animatable))
    val dodge = addSkill(SwordDodge(animatable))
    val special = addSkill(剑战技(animatable))

    val shieldComboC1 = addSkill(ShieldComboC1(animatable))
    val shieldComboC2 = addSkill(ShieldComboC2(animatable))
    val shieldGuard = addSkill(ShieldGuardSkill(animatable))

    init {
        addComponent(ShieldComboControlComponent(combo0, shieldComboC1, shieldComboC2, combo2))

        addComponent(HoldReleaseSkillControlComponent(special))
        addComponent(JumpAttackControlComponent(jumpAttack))
        addComponent(SprintAttackControlComponent(sprintingAttack))
        addComponent(ChangeableComboControlComponent(combo0, combo1, combo2))
        addComponent(DodgeControlComponent(dodge))

        addComponent(ShieldGuardControlComponent(shieldGuard))
        addComponent(GuardControlComponent(guard))
    }

    override fun isAvailable(): Boolean {
        return holder.mainHandItem.`is`(ItemTags.SWORDS)
    }

}