package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC1
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC2
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldGuardSkill
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeCombo0
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeCombo1
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeCombo2
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeGuard
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeJumpAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.斧子投技
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.AxeSprintingAttack
import cn.solarmoon.spirit_of_fight.skill.controller.component.ChangeableComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.DodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.GuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.JumpAttackControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ShieldComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ShieldGuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SpecialSkillControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3

class AxeFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("axe", holder, animatable, 1.0, false) {

    override val boxLength: DVector3 = DVector3(0.75, 0.75, 0.75)
    override val boxOffset: DVector3 = DVector3(0.0, 0.0, -0.55)

    val combo0 = addSkill(AxeCombo0(animatable))
    val combo1 = addSkill(AxeCombo1(animatable))
    val combo2 = addSkill(AxeCombo2(animatable))
    val sprintingAttack = addSkill(AxeSprintingAttack(animatable))
    val jumpAttack = addSkill(AxeJumpAttack(animatable))
    val guard = addSkill(AxeGuard(animatable))
    val dodge = addSkill(AxeDodge(animatable))
    val special = addSkill(斧子投技(animatable))

    val shieldComboC1 = addSkill(ShieldComboC1(animatable))
    val shieldComboC2 = addSkill(ShieldComboC2(animatable))
    val shieldGuard = addSkill(ShieldGuardSkill(animatable))

    init {
        addComponent(ShieldComboControlComponent(combo0, shieldComboC1, shieldComboC2, combo2))

        addComponent(SpecialSkillControlComponent(special, false))
        addComponent(JumpAttackControlComponent(jumpAttack))
        addComponent(SprintAttackControlComponent(sprintingAttack))
        addComponent(ChangeableComboControlComponent(combo0, combo1, combo2))
        addComponent(DodgeControlComponent(dodge))
        addComponent(ShieldGuardControlComponent(shieldGuard))
        addComponent(GuardControlComponent(guard))
    }

    override fun isAvailable(): Boolean {
        return holder.mainHandItem.`is`(ItemTags.AXES)
    }

}