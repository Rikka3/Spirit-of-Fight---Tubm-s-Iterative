package cn.solarmoon.spirit_of_fight.skill.concrete.axe

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import net.minecraft.world.entity.LivingEntity

class AxeDodge(
    holder: IEntityAnimatable<out LivingEntity>
): DodgeSkill(holder, "axe:dodge") {
}