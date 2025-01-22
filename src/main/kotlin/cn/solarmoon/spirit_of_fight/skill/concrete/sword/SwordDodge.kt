package cn.solarmoon.spirit_of_fight.skill.concrete.sword

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import net.minecraft.world.entity.LivingEntity

class SwordDodge(
    holder: IEntityAnimatable<out LivingEntity>
): DodgeSkill(holder, "sword:dodge") {
}