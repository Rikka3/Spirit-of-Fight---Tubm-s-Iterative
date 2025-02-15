package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.node.BehaviorNode
import cn.solarmoon.spark_core.skill.node.NodeStatus
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class PreventMoveWithBackComponent(
    val activeTime: List<Vec2> = listOf()
): BehaviorNode() {

    override fun onTick(skill: SkillInstance): NodeStatus {
        val time = require<() -> Double>("time").invoke()
        if (activeTime.any { time in it.x..it.y } || activeTime.isEmpty()) {
            val player = skill.holder as? Player ?: return NodeStatus.FAILURE
            if (player.isLocalPlayer) {
                if ((player as LocalPlayer).savedInput.forwardImpulse < 0) {
                    player.deltaMovement = Vec3(0.0, player.deltaMovement.y, 0.0)
                }
            }
        }
        return NodeStatus.RUNNING
    }

    override val codec: MapCodec<out BehaviorNode> = CODEC

    override fun copy(): BehaviorNode {
        return PreventMoveWithBackComponent(activeTime)
    }

    companion object {
        val CODEC: MapCodec<PreventMoveWithBackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                SerializeHelper.VEC2_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime }
            ).apply(it, ::PreventMoveWithBackComponent)
        }
    }

}