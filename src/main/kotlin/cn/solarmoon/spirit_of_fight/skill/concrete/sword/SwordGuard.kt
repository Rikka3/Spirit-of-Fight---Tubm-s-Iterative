package cn.solarmoon.spirit_of_fight.skill.concrete.sword

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.LivingEntity

class SwordGuard(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "sword:guard", true) {

    override val guardSound: SoundEvent = SOFSounds.SHARP_BLOCK.get()
    override val parrySound: SoundEvent = SOFSounds.SHARP_PARRY.get()

}