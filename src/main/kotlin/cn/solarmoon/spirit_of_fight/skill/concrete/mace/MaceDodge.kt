package cn.solarmoon.spirit_of_fight.skill.concrete.mace

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import net.minecraft.world.entity.LivingEntity

class MaceDodge(
    holder: IEntityAnimatable<out LivingEntity>
): DodgeSkill(holder, "mace:dodge") {
}