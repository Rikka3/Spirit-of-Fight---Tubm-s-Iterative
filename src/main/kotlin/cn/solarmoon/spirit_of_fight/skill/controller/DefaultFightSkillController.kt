package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.barehand.BareHandCombo0
import cn.solarmoon.spirit_of_fight.skill.concrete.barehand.BareHandCombo1
import cn.solarmoon.spirit_of_fight.skill.concrete.barehand.BareHandDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.barehand.BareHandSprintingAttack
import cn.solarmoon.spirit_of_fight.skill.controller.component.ComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.DodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3

class DefaultFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("default", holder, animatable, 1.6, false) {

    override val boxLength: DVector3
        get() = DVector3(1.0, 1.0, 1.0)
    override val boxOffset: DVector3
        get() = DVector3()

    override fun isAvailable(): Boolean {
        return true
    }

    val combo0 = addSkill(BareHandCombo0(animatable))
    val combo1 = addSkill(BareHandCombo1(animatable))
    val sprintAttack = addSkill(BareHandSprintingAttack(animatable))
    val dodge = addSkill(BareHandDodge(animatable))

    init {
        addComponent(ComboControlComponent(combo0, combo1))
        addComponent(SprintAttackControlComponent(sprintAttack))
        addComponent(DodgeControlComponent(dodge))
    }


}