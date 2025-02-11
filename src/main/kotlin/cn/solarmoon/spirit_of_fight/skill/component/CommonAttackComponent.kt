package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.phys.attack.CommonAttackCollisionCallback
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class CommonAttackComponent(
    val preFirstAttackComponents: List<SkillComponent> = listOf(),
    val preAttackComponents: List<SkillComponent> = listOf()
): SkillComponent() {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return CommonAttackComponent(preFirstAttackComponents.map { it.copy() }, preAttackComponents.map { it.copy() })
    }

    override fun onActive() {
        requireQuery<MutableList<PhysicsRigidBody>>("rigid_body.attack").forEach {
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
                        setCustomActive(preFirstAttackComponents)
                    }
                    setCustomActive(preAttackComponents)
                }
            })
        }
    }

    override fun onUpdate() {}

    override fun onEnd() {}

    companion object {
        val CODEC: MapCodec<CommonAttackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                SkillComponent.CODEC.listOf().optionalFieldOf("on_pre_first_attack", listOf()).forGetter { it.preFirstAttackComponents },
                SkillComponent.CODEC.listOf().optionalFieldOf("on_pre_attack", listOf()).forGetter { it.preAttackComponents }
            ).apply(it, ::CommonAttackComponent)
        }
    }

}