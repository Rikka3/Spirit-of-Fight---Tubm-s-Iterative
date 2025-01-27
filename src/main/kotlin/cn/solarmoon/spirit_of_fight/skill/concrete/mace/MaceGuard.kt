package cn.solarmoon.spirit_of_fight.skill.concrete.mace

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.LivingEntity

class MaceGuard(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "mace:guard", true) {

    override val guardSound: SoundEvent = SOFSounds.HARD_BLOCK.get()
    override val parrySound: SoundEvent = SOFSounds.SHARP_PARRY.get()

}