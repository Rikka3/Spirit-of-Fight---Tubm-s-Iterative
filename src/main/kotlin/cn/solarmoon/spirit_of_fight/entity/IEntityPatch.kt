package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.entity.grab.GrabManager
import cn.solarmoon.spirit_of_fight.poise_system.PoiseData

interface IEntityPatch {

    var moveDirection: MoveDirection?

    val poise: PoiseData

    val grabManager: GrabManager

    var isHitting: Boolean

    var isKnockedDown: Boolean

    var wieldStyle: WieldStyle

}