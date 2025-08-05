package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.js.call
import cn.solarmoon.spark_core.js.extension.JSSkill
import cn.solarmoon.spirit_of_fight.skill.BlockSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import org.mozilla.javascript.Function

interface JSSOFSkill: JSSkill {

    fun onPerfectDodge(consumer: Function) = skill.onEvent<DodgeSkill.PerfectDodge> {
        consumer.call(js, it.event, (skill as? DodgeSkill)?.perfectDodgeCheck ?: 0)
    }

    fun onBlockHurt(consumer: Function) = skill.onEvent<BlockSkill.BlockHurt> {
        consumer.call(js, it.event, it.hitPos)
    }

    fun onPrecisionBlock(consumer: Function) = skill.onEvent<BlockSkill.PrecisionBlock> {
        consumer.call(js, it.event, it.hitPos)
    }

}
