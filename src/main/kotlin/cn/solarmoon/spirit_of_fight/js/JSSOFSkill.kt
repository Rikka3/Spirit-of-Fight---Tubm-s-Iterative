package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.js.call
import cn.solarmoon.spark_core.js.extension.JSSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.GuardSkill
import org.mozilla.javascript.Function

interface JSSOFSkill: JSSkill {

    fun onPerfectDodge(consumer: Function) = skill.onEvent<DodgeSkill.PerfectDodge> {
        consumer.call(js, it.event, (skill as? DodgeSkill)?.perfectDodgeCheck ?: 0)
    }

    fun onGuardHurt(consumer: Function) = skill.onEvent<GuardSkill.GuardHurt> {
        consumer.call(js, it.event, it.hitPos)
    }

    fun onParry(consumer: Function) = skill.onEvent<GuardSkill.Parry> {
        consumer.call(js, it.event, it.hitPos)
    }

}
