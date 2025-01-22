package cn.solarmoon.spirit_of_fight.hit.type

import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spirit_of_fight.hit.AttackStrength
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom

open class Swipe(
    override val strength: AttackStrength,
    override val indefensible: Boolean
): BaseHitType() {



}