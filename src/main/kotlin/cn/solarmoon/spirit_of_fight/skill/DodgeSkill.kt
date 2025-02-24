package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.registry.common.SparkSkillContext
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec2
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

open class DodgeSkill(
    val animIndex: AnimIndex,
    val transitionTime: Int,
    val invincibleTime: List<Vec2>,
    val perfectDodgeTime: List<Vec2>,
    val perfectDodgeOnlyOnce: Boolean = true,
    val onAnimLifeCycle: List<SkillComponent> = listOf(),
    val onPerfectDodge: List<SkillComponent> = listOf(),
): Skill() {

    var animation: AnimInstance? = null
    private var perfectDodgeCheck = false

    override fun onEvent(event: Event) {
        val animation = animation
        if (animation == null) return

        if (event is LivingIncomingDamageEvent) {
            val victim = event.entity
            val time = animation.time
            if (victim == holder) {
                if (invincibleTime.isEmpty() || invincibleTime.any { time in it.x..it.y }) {
                    if (!perfectDodgeCheck && perfectDodgeTime.any { time in it.x..it.y }) {
                        onPerfectDodge(time, event)
                        if (perfectDodgeOnlyOnce) perfectDodgeCheck = true
                    }
                    event.isCanceled = true
                }
            }
        }
    }

    open fun onPerfectDodge(time: Double, event: LivingIncomingDamageEvent) {
        onPerfectDodge.forEach { it.attach(this) }
        PacketDistributor.sendToAllPlayers(SkillPayload(this))
    }

    override fun sync(data: CompoundTag, context: IPayloadContext) {
        onPerfectDodge.forEach { it.attach(this) }
    }

    override fun onActive() {
        val animatable = holder as? IEntityAnimatable<*> ?: return
        val entity = animatable.animatable

        perfectDodgeCheck = false

        val direction = entity.moveDirection ?: run { end(); return }

        animation = AnimInstance.create(animatable, AnimIndex(animIndex.index, animIndex.name + "_${direction.toString().lowercase()}")) {
            shouldTurnBody = true

            onEvent<AnimEvent.SwitchIn> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onAnimLifeCycle.forEach { it.attach(this@DodgeSkill) }
            }

            onEvent<AnimEvent.Tick> {
                blackBoard.write(SparkSkillContext.TIME, time)
                onAnimLifeCycle.forEach { it.tick() }
            }

            onEvent<AnimEvent.End> {
                end()
            }
        }

        animatable.animController.setAnimation(animation, transitionTime)
    }

    override fun onEnd() {
        animation?.cancel()
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<DodgeSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                AnimIndex.CODEC.fieldOf("anim_index").forGetter { it.animIndex },
                Codec.INT.fieldOf("transition_time").forGetter { it.transitionTime },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("invincible_time", listOf()).forGetter { it.invincibleTime },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("perfect_dodge_time", listOf()).forGetter { it.perfectDodgeTime },
                Codec.BOOL.optionalFieldOf("only_one_perfect_dodge_allowed", true).forGetter { it.perfectDodgeOnlyOnce },
                SkillComponent.CODEC.listOf().fieldOf("on_anim_life_cycle").forGetter { it.onAnimLifeCycle },
                SkillComponent.CODEC.listOf().fieldOf("on_perfect_dodge").forGetter { it.onPerfectDodge },
            ).apply(it, ::DodgeSkill)
        }
    }

}