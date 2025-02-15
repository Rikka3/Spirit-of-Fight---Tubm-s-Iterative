package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.node.BehaviorNode
import cn.solarmoon.spark_core.skill.node.NodeStatus
import cn.solarmoon.spark_core.skill.node.leaves.EmptyNode
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.phys.Vec2
import kotlin.ranges.contains

class PerfectGuardComponent(
    val triggerOnlyOnce: Boolean = true,
    val activeTime: List<Vec2> = listOf(),
    val onPerfectGuard: BehaviorNode = EmptyNode.Success
): BehaviorNode() {

    init {
        dynamicContainer.addChild(onPerfectGuard)
    }

    override fun onStart(skill: SkillInstance) {
        if (read<Boolean>("perfect_guard_check") == true && triggerOnlyOnce) return
        val immuneTime = require<Double>("on_hurt.time")
        if (activeTime.any { immuneTime in it.x..it.y } || activeTime.isEmpty()) {
            onPerfectGuard.tick(skill)
            write("perfect_guard_check", true)
        }
    }

    override fun onTick(skill: SkillInstance): NodeStatus {
        return NodeStatus.SUCCESS
    }

    override val codec: MapCodec<out BehaviorNode> = CODEC

    override fun copy(): BehaviorNode {
        return PerfectGuardComponent(triggerOnlyOnce, activeTime, onPerfectGuard.copy())
    }

    companion object {
        val CODEC: MapCodec<PerfectGuardComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.optionalFieldOf("trigger_only_once", true).forGetter { it.triggerOnlyOnce },
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime },
                BehaviorNode.CODEC.optionalFieldOf("on_perfect_dodge", EmptyNode.Success).forGetter { it.onPerfectGuard }
            ).apply(it, ::PerfectGuardComponent)
        }
    }

}