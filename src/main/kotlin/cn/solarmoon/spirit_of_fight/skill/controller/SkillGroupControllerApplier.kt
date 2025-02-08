package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

object SkillGroupControllerApplier {

    @SubscribeEvent
    private fun entityTick(event: EntityTickEvent.Post) {
        val entity = event.entity
        val skillGroup = entity.activeSkillGroup ?: return
        skillGroup.controllers.forEach {
            if (it is PreInputCommonReleaseController && !entity.isPlayingSkill) entity.getPreInput().executeIfPresent()
        }
    }

}