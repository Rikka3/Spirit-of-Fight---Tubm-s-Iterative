package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.feature.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBody
import org.ode4j.ode.DBox

class ShieldParrySkill(
    holder: IEntityAnimatable<out LivingEntity>
): ParrySkill(holder, "shield:parry", createSkillAttackAnimBody("leftItem", holder, holder.animatable.level(), holder.animatable.getPatch().offAttackSystem) {
    (firstGeom as? DBox)?.let {
        it.lengths = DVector3(0.5, 1.0, 1.55)
        it.offsetPosition = DVector3(-0.25, 0.0, -0.25)
    }
}) {

}