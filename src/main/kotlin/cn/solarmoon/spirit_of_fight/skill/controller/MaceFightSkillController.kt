package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC1
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldComboC2
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldGuardSkill
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceCombo0
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceCombo1
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceCombo2
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceDodge
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceGuard
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceJumpAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.MaceSprintingAttack
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.天神下凡
import cn.solarmoon.spirit_of_fight.skill.concrete.mace.锤战技
import cn.solarmoon.spirit_of_fight.skill.controller.component.ChangeableComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.DodgeControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.GuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.JumpAttackControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.HoldReleaseSkillControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.FallAttackControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ShieldComboControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.ShieldGuardControlComponent
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.common.Tags
import org.ode4j.math.DVector3

class MaceFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("mace", holder, animatable, 0.6, false) {

    override val boxLength: DVector3 = DVector3(0.95, 0.95, 0.95)
    override val boxOffset: DVector3 = DVector3(0.0, 0.0, -0.65)

    val combo0 = addSkill(MaceCombo0(animatable))
    val combo1 = addSkill(MaceCombo1(animatable))
    val combo2 = addSkill(MaceCombo2(animatable))
    val sprintingAttack = addSkill(MaceSprintingAttack(animatable))
    val jumpAttack = addSkill(MaceJumpAttack(animatable))
    val guard = addSkill(MaceGuard(animatable))
    val dodge = addSkill(MaceDodge(animatable))
    val special = addSkill(锤战技(animatable))
    val jumpSpecial = addSkill(天神下凡(animatable))

    val shieldComboC1 = addSkill(ShieldComboC1(animatable))
    val shieldComboC2 = addSkill(ShieldComboC2(animatable))
    val shieldGuard = addSkill(ShieldGuardSkill(animatable))

    init {
        addComponent(FallAttackControlComponent(jumpSpecial))
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
        return holder.mainHandItem.`is`(Tags.Items.TOOLS_MACE)
    }

}