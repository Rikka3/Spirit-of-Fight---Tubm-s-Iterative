package cn.solarmoon.spirit_of_fight.skill.concrete.hammer

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.feature.body.createGuardAnimBody
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBody
import org.ode4j.ode.DBox

class HammerGuard(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "hammer:guard", createGuardAnimBody("rightItem", holder, holder.animatable.level()) {
    (firstGeom as? DBox)?.apply {
        lengths = DVector3(0.65, 0.65, 2.0)
        offsetPosition = DVector3(0.0, 0.0, -0.5)
    }
}) {

}