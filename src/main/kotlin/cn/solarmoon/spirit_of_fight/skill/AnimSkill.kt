package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.PhysicsBodyAttackComponent
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

open class AnimSkill(
    val animPlayer: PlayAnimationComponent
): Skill() {

    override fun onActive(): Boolean {
        animPlayer.attach(this)
        animPlayer.anim.apply {
            shouldTurnBody = true
            onEvent<AnimEvent.End> {
                end()
            }
        }

        return true
    }

    override fun onEnd() {
        animPlayer.anim.cancel()
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<AnimSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                PlayAnimationComponent.CODEC.fieldOf("animation").forGetter { it.animPlayer }
            ).apply(it, ::AnimSkill)
        }
    }

}