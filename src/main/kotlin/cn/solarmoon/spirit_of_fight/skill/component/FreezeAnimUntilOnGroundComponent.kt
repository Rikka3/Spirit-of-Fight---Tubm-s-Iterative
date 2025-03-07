package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class FreezeAnimUntilOnGroundComponent(
    val startTime: Double,
    val speed: Double = 0.0
): SkillComponent() {

    var speedCache = 0.0
    var check = false
    var anim: AnimInstance? = null

    override fun onAttach(): Boolean {
        val animatable = skill.holder as? IAnimatable<*> ?: return false
        val entity = skill.holder as? Entity ?: return false
        speedCache = animatable.animController.getPlayingAnim()?.speed ?: 0.0
        animatable.animController.getPlayingAnim()?.onEvent<AnimEvent.PhysicsTick> {
            anim = this
            if (time >= startTime && !check) {
                check = true
                speed = this@FreezeAnimUntilOnGroundComponent.speed
            }

            if (check && entity.onGround() && speedCache != -1.0) {
                anim?.speed = speedCache
                speedCache = -1.0
            }
        }
        return true
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<FreezeAnimUntilOnGroundComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.DOUBLE.fieldOf("start_time").forGetter { it.startTime },
                Codec.DOUBLE.optionalFieldOf("speed", 0.0).forGetter { it.speed }
            ).apply(it, ::FreezeAnimUntilOnGroundComponent)
        }
    }

}