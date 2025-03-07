package cn.solarmoon.spirit_of_fight.event

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.TypedAnimation
import cn.solarmoon.spark_core.util.Side
import net.neoforged.neoforge.event.entity.EntityEvent

class GetHitAnimationEvent(
    val animatable: IEntityAnimatable<*>,
    val hitType: String,
    val boneName: String,
    val posSide: Side,
    val hitSide: Side
): EntityEvent(animatable.animatable) {

    var resultHitAnim: TypedAnimation? = null

}