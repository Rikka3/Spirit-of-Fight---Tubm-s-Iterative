package cn.solarmoon.spirit_of_fight.skill.component.attack

import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.physics.presets.AttackContactListener
import com.jme3.bullet.collision.PersistentManifolds
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

class CommonAttackContactListener: AttackContactListener {

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
        if (attacker is Player) {
            attacker.attack(target)
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

    override fun onContactStarted(manifoldId: Long) {
        val a = PhysicsCollisionObject.findInstance(PersistentManifolds.getBodyAId(manifoldId))
        val b = PhysicsCollisionObject.findInstance(PersistentManifolds.getBodyBId(manifoldId))
        val attacker = a.owner as? Entity ?: return
        (b.owner as? Entity)?.apply {
            attackSystem.customAttack(this) {
                preAttack(attacker, this@apply, a, b, manifoldId)
                if (!doAttack(attacker, this@apply, a, b, manifoldId)) return@customAttack false
                postAttack(attacker, this@apply, a, b, manifoldId)
                true
            }
        }
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