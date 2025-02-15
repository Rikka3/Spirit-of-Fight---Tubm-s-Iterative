package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.node.BehaviorNode
import cn.solarmoon.spark_core.skill.node.NodeStatus
import cn.solarmoon.spark_core.skill.node.leaves.EmptyNode
import cn.solarmoon.spirit_of_fight.phys.attack.CommonAttackCollisionCallback
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class CommonAttackComponent(
    val preFirstAttackComponents: BehaviorNode = EmptyNode.Success,
    val preAttackComponents:BehaviorNode = EmptyNode.Success
): BehaviorNode() {

    init {
        dynamicContainer.addChild(preFirstAttackComponents)
        dynamicContainer.addChild(preAttackComponents)
    }

    override fun onStart(skill: SkillInstance) {
        require<MutableList<PhysicsRigidBody>>("rigid_body.attack").forEach {
            it.addCollisionCallback(object : CommonAttackCollisionCallback() {
                override fun preAttack(
                    attacker: Entity,
                    target: Entity,
                    aBody: PhysicsCollisionObject,
                    bBody: PhysicsCollisionObject,
                    manifoldId: Long
                ) {
                    super.preAttack(attacker, target, aBody, bBody, manifoldId)
                    if (skill.level.isClientSide) return
                    if (attackSystem.attackedEntities.isEmpty()) {
                        preFirstAttackComponents.tick(skill)
                    }
                    preAttackComponents.tick(skill)
                }
            })
        }
    }

    override fun onTick(skill: SkillInstance): NodeStatus {
        return NodeStatus.SUCCESS
    }

    override val codec: MapCodec<out BehaviorNode> = CODEC

    override fun copy(): BehaviorNode {
        return CommonAttackComponent(preFirstAttackComponents.copy(), preAttackComponents.copy())
    }

    companion object {
        val CODEC: MapCodec<CommonAttackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                BehaviorNode.CODEC.optionalFieldOf("on_pre_first_attack", EmptyNode.Success).forGetter { it.preFirstAttackComponents },
                BehaviorNode.CODEC.optionalFieldOf("on_pre_attack", EmptyNode.Success).forGetter { it.preAttackComponents }
            ).apply(it, ::CommonAttackComponent)
        }
    }

}