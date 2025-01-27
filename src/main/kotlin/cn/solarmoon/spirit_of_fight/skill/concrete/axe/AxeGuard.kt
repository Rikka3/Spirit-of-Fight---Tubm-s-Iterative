package cn.solarmoon.spirit_of_fight.skill.concrete.axe

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import net.minecraft.world.entity.LivingEntity

class AxeGuard(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "axe:guard", true) {



}