package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import net.minecraft.world.entity.LivingEntity

class BaimeiDodge(
    holder: IEntityAnimatable<out LivingEntity>
): DodgeSkill(holder, "baimei:dodge") {
}