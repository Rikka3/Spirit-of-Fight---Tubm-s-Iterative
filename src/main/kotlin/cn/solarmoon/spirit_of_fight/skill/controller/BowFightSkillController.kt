package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.滑铲
import cn.solarmoon.spirit_of_fight.skill.controller.component.SprintAttackControlComponent
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.common.Tags
import org.ode4j.math.DVector3

class BowFightSkillController(
    holder: LivingEntity,
    animatable: IEntityAnimatable<out LivingEntity>
): FightSkillController<LivingEntity>("bow", holder, animatable, 4.0, true) {

    override val boxLength: DVector3 = DVector3(1.0, 1.0, 1.0)
    override val boxOffset: DVector3 = DVector3(0.0, 0.0, 0.0)

    val sprintingAttack = addSkill(滑铲(animatable))

    init {
        addComponent(SprintAttackControlComponent(sprintingAttack))
    }

    override fun isAvailable(): Boolean {
        return holder.mainHandItem.`is`(Tags.Items.TOOLS_BOW)
    }

}