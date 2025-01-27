package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.LivingEntity

class BaimeiGuard(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "baimei:guard", false) {

    override val guardSound: SoundEvent = SOFSounds.SOFT_BLOCK.get()

}