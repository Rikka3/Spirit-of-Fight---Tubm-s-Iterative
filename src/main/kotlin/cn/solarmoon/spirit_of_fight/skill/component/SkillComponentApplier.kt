package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.controller.getSkillController
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

object SkillComponentApplier {

    @SubscribeEvent
    private fun onHurt(event: LivingIncomingDamageEvent) {
        val entity = event.entity
        entity.getSkillController()?.allSkills?.values?.forEach {
            it.components.forEach {
                if (it is AnimImmunityToDamageComponent) it.onHurt(event)
                if (it is AnimGuardComponent && it.isEnabled) it.onHurt(event)
            }
        }
    }

}