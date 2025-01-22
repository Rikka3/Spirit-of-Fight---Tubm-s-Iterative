package cn.solarmoon.spirit_of_fight.skill.concrete.sword

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ParrySkill
import net.minecraft.world.entity.LivingEntity

class SwordParry(
    holder: IEntityAnimatable<out LivingEntity>
): ParrySkill(holder, "sword:parry") {
}