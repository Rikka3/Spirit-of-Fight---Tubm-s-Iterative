package cn.solarmoon.spirit_of_fight.skill.component.collision

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spirit_of_fight.phys.attack.CommonAttackContactListener
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity

class CommonAttackCollisionComponent(
    val preAttackComponents: List<Pair<Boolean, SkillComponent>> = listOf(),
    children: List<SkillComponent> = listOf()
): SkillComponent(children) {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return CommonAttackCollisionComponent(preAttackComponents.map { Pair(it.first, it.second.copy()) }, children)
    }

    override fun onActive(): Boolean {
        requireQuery<PhysicsCollisionObject>("rigid_body").addContactListener(object : CommonAttackContactListener() {
            override fun preAttack(
                attacker: Entity,
                target: Entity,
                aBody: PhysicsCollisionObject,
                bBody: PhysicsCollisionObject,
                manifoldId: Long
            ) {
                super.preAttack(attacker, target, aBody, bBody, manifoldId)
                if (skill.level.isClientSide) return
                preAttackComponents.forEach { p ->
                    if (!p.first || !attackSystem.hasAttacked(target)) p.second.active(skill)
                }
            }
        })
        return true
    }

    override fun onUpdate(): Boolean {
        return true
    }

    override fun onEnd(): Boolean {
        return true
    }

    companion object {
        val CODEC: MapCodec<CommonAttackCollisionComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.pair(
                    Codec.BOOL.optionalFieldOf("only_when_attack_first_target", false).codec(),
                    SkillComponent.CODEC.fieldOf("component").codec()
                ).listOf().optionalFieldOf("on_pre_attack", listOf()).forGetter { it.preAttackComponents },
                SkillComponent.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children }
            ).apply(it, ::CommonAttackCollisionComponent)
        }
    }

}