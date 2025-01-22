package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerCombo0
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerCombo1
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerCombo2
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerComboC1
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerComboC2
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerGuard
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerJumpAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.HammerSprintingAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.hammer.大锤猛击
import cn.solarmoon.spirit_of_fight.skill.controller.component.ComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.DodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.GuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.HammerSecComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.JumpAttackControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SpecialSkillControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3

class HammerFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("axe", holder, animatable, 1.0, true) {

    override val boxLength: DVector3 = DVector3(0.75, 1.25, 0.75)
    override val boxOffset: DVector3 = DVector3(0.0, 0.0, -1.125)

    val combo0 = addSkill(HammerCombo0(animatable))
    val combo1 = addSkill(HammerCombo1(animatable))
    val combo2 = addSkill(HammerCombo2(animatable))
    val comboC1 = addSkill(HammerComboC1(animatable))
    val comboC2 = addSkill(HammerComboC2(animatable))
    val sprintingAttack = addSkill(HammerSprintingAttack(animatable))
    val jumpAttack = addSkill(HammerJumpAttack(animatable))
    val dodge = addSkill(HammerDodge(animatable))
    val guard = addSkill(HammerGuard(animatable))
    val special = addSkill(大锤猛击(animatable))

    init {
        addComponent(HammerSecComboControlComponent(combo0, comboC1, comboC2, combo2))

        addComponent(SpecialSkillControlComponent(special, true))
        addComponent(JumpAttackControlComponent(jumpAttack))
        addComponent(SprintAttackControlComponent(sprintingAttack))
        addComponent(ComboControlComponent(combo0, combo1, combo2))
        addComponent(DodgeControlComponent(dodge))
        addComponent(GuardControlComponent(guard))
    }

    override fun isAvailable(): Boolean {
        return holder.mainHandItem.`is`(SOFItemTags.FORGE_HAMMER) && holder.offhandItem.isEmpty
    }

}