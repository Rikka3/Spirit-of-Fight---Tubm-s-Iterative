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
    val onPerfectDodge: List<SkillComponent> = listOf(),
    children: List<SkillComponent> = listOf()
): SkillComponent(children) {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return PerfectDodgeComponent(triggerOnlyOnce, activeTime, onPerfectDodge.map { it.copy() }, children)
    }

    class Check

    override fun onActive(): Boolean {
        if (query<Boolean>("perfect_dodge_check") == true && triggerOnlyOnce) return true
        val immuneTime = requireQuery<Double>("on_hurt.time")
        if (activeTime.any { immuneTime in it.x..it.y } || activeTime.isEmpty()) {
            onPerfectDodge.forEach { it.active(skill) }
            addContext("perfect_dodge_check", true)
        }
        return true
    }

    override fun onUpdate(): Boolean {
        return true
    }

    override fun onEnd(): Boolean {
        return true
    }

    companion object {
        val CODEC: MapCodec<PerfectDodgeComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.optionalFieldOf("trigger_only_once", true).forGetter { it.triggerOnlyOnce },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_perfect_dodge", listOf()).forGetter { it.onPerfectDodge },
                SkillComponent.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children }
            ).apply(it, ::PerfectDodgeComponent)
        }
    }

}