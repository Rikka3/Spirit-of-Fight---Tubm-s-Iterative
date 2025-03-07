package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.preinput.PreInputId

object SOFPreInputs {

    val DODGE = PreInputId("dodge")
    val ATTACK = PreInputId("attack")
    val GUARD = PreInputId("guard")
    val MOVE = PreInputId("move", -1)
    val JUMP = PreInputId("jump")
    val STOP = PreInputId("stop")
    val SPECIAL_ATTACK = PreInputId("special_attack")

}