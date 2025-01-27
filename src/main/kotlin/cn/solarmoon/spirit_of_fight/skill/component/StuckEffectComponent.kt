package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.SkillComponent
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.TriggeredSkillComponent
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import org.ode4j.ode.DBody
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom

class StuckEffectComponent(
    val time: Int = 5,
    val speed: Double = 0.05,
    val enable: () -> Boolean = { true }
): SkillComponent {

    fun whenTargetAttacked(o1: DGeom, o2: DGeom, buffer: DContactBuffer, attackSystem: AttackSystem) {
        val entity = o1.body.owner as? IEntityAnimatable<*> ?: return
        if (enable.invoke()) {
            entity.animController.changeSpeed(time, speed)
        }
    }

}