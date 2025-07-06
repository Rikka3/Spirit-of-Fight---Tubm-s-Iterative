package cn.solarmoon.spirit_of_fight.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import java.awt.Color

class ShockEffect: MobEffect(MobEffectCategory.HARMFUL, Color.DARK_GRAY.rgb) {

    override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
        if (livingEntity is Mob) livingEntity.isNoAi = true
        return true
    }



}