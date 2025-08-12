package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillEvent
import cn.solarmoon.spark_core.skill.read
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class DodgeSkill: Skill() {
    class PerfectDodge(val event: LivingIncomingDamageEvent): SkillEvent()

    var perfectDodgeCheck = 0

    fun perfectDodge(event: LivingIncomingDamageEvent) {
        val maxPerfectDodgeTimes = (config.read("max_perfect_dodge_times", 1.0))
        if (perfectDodgeCheck < maxPerfectDodgeTimes) {
            triggerEvent(PerfectDodge(event))
            perfectDodgeCheck++
        }
        event.isCanceled = true
    }

}