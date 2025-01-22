package cn.solarmoon.spirit_of_fight.skill.concrete.mace

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ParrySkill
import net.minecraft.world.entity.LivingEntity

class MaceParry(
    holder: IEntityAnimatable<out LivingEntity>
): ParrySkill(holder, "mace:parry") {
}