package cn.solarmoon.spirit_of_fight.skill.concrete.sword

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import net.minecraft.world.entity.LivingEntity

class SwordGuard(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "sword:guard", true) {
}