package cn.solarmoon.spirit_of_fight.hit.type

import cn.solarmoon.spirit_of_fight.hit.AttackStrength

open class Chop(
    override val strength: AttackStrength,
    override val indefensible: Boolean
): BaseHitType() {



}