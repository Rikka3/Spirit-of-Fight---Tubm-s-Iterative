package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.TriggeredSkillComponent

class AnimPreInputAcceptComponent(
    val acceptNode: Double,
    val preInput: PreInput,
    val anim: AnimInstance,
    val limit: AnimInstance.(String) -> Boolean =  { true },
    val extraInputNode: AnimInstance.(PreInput) -> Unit = {},
    val action: () -> Unit = {}
): TriggeredSkillComponent {

    override fun start() {
        anim.onTick {
            if (time >= acceptNode && limit.invoke(anim, preInput.id)) {
                preInput.executeIfPresent("", action)
            }
        }

        extraInputNode.invoke(anim, preInput)
    }

    override fun stop() {}

}