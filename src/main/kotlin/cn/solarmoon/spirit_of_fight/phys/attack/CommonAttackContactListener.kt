package cn.solarmoon.spirit_of_fight.phys.attack

import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.physics.presets.AttackContactListener
import com.jme3.bullet.collision.PersistentManifolds
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

open class CommonAttackContactListener: AttackContactListener {

    override val attackSystem: AttackSystem = AttackSystem()

    override fun preAttack(
        attacker: Entity,
        target: Entity,
        aBody: PhysicsCollisionObject,
        bBody: PhysicsCollisionObject,
        manifoldId: Long
    ) {

    }

    override fun doAttack(
        attacker: Entity,
        target: Entity,
        aBody: PhysicsCollisionObject,
        bBody: PhysicsCollisionObject,
        manifoldId: Long
    ): Boolean {
        if (attacker.level().isClientSide) return false
        if (attacker is Player) {
            attacker.attack(target)
            return true
        } else if (attacker is LivingEntity) {
            attacker.doHurtTarget(target)
            return true
        }
        return false
    }

    override fun postAttack(
        attacker: Entity,
        target: Entity,
        aBody: PhysicsCollisionObject,
        bBody: PhysicsCollisionObject,
        manifoldId: Long
    ) {

    }

    override fun onContactEnded(manifoldId: Long) {

    }

    override fun onContactProcessed(
        pcoA: PhysicsCollisionObject?,
        pcoB: PhysicsCollisionObject?,
        manifoldPointId: Long
    ) {

    }

}