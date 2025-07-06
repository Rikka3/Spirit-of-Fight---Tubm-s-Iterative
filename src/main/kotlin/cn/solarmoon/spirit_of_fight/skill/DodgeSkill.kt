package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.js.skill.JSSkillTypeBuilder
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class DodgeSkill: Skill() {
    class PerfectDodge(val event: LivingIncomingDamageEvent): SkillEvent()

    var perfectDodgeCheck = 0

    init {
        init {
            val animatable = holder as? IEntityAnimatable<*> ?: return@init
            val entity = animatable.animatable
            val direction = entity.moveDirection ?: return@init
            val anim = AnimInstance.create(animatable, "${config["anim_name"]!! as String}_${direction.toString().lowercase()}")
            config.put("animation", anim)
        }
    }

    fun perfectDodge(event: LivingIncomingDamageEvent) {
        val maxPerfectDodgeTimes = (config["max_perfect_dodge_times"] as? Double)?.toInt() ?: 0
        if (perfectDodgeCheck < maxPerfectDodgeTimes) {
            triggerEvent(PerfectDodge(event))
            perfectDodgeCheck++
        }
        event.isCanceled = true
    }

}