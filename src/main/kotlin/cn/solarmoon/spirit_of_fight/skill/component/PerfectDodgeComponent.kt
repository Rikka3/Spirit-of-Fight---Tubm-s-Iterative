package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.component.InvincibilityComponent
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec2

class PerfectDodgeComponent(
    val triggerOnlyOnce: Boolean = true,
    val activeTime: List<Vec2> = listOf(),
    val onPerfectDodge: List<SkillComponent> = listOf()
): SkillComponent() {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return PerfectDodgeComponent(triggerOnlyOnce, activeTime, onPerfectDodge.map { it.copy() })
    }

    override fun onActive() {
        if (query<Boolean>("perfect_dodge_check") == true && triggerOnlyOnce) return
        val immuneTime = requireQuery<Double>("on_hurt.time")
        if (activeTime.any { immuneTime in it.x..it.y } || activeTime.isEmpty()) {
            setCustomActive(onPerfectDodge)
            addOrReplaceContext("perfect_dodge_check", true)
        }
    }

    override fun onUpdate() {}

    override fun onEnd() {}

    companion object {
        val CODEC: MapCodec<PerfectDodgeComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.optionalFieldOf("trigger_only_once", true).forGetter { it.triggerOnlyOnce },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_perfect_dodge", listOf()).forGetter { it.onPerfectDodge }
            ).apply(it, ::PerfectDodgeComponent)
        }
    }

}