package cn.solarmoon.spirit_of_fight.entity

import net.minecraft.world.entity.LivingEntity

interface ICustomMeleeAttackEntity {

    fun meleeAttack(target: LivingEntity)

}