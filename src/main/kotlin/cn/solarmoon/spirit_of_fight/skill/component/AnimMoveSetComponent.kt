package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.TriggeredSkillComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

class AnimMoveSetComponent(
    val entity: Entity,
    val anim: AnimInstance,
    val moveSet: AnimInstance.() -> Vec3?
): TriggeredSkillComponent {

    override fun start() {
        anim.onTick {
            moveSet.invoke(anim)?.let {
                entity.deltaMovement = it
            }
        }
    }

    override fun stop() {}

}