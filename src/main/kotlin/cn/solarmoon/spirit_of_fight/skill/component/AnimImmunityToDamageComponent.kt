package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.SkillComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class AnimImmunityToDamageComponent(
    val victim: Entity,
    val anim: AnimInstance,
    val onSuccessImmunity: LivingIncomingDamageEvent.() -> Unit = {},
    val enableImmunity: AnimInstance.(LivingIncomingDamageEvent) -> Boolean
): SkillComponent {

    fun onHurt(event: LivingIncomingDamageEvent) {
        val entity = event.entity
        if (anim.isCancelled) return

        if (entity == victim && enableImmunity.invoke(anim, event)) {
            onSuccessImmunity.invoke(event)
            event.isCanceled = true
        }
    }

}