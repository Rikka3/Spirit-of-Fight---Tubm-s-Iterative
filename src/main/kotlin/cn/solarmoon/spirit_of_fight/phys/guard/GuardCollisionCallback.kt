package cn.solarmoon.spirit_of_fight.phys.guard

import cn.solarmoon.spark_core.physics.collision.CollisionCallback
import com.jme3.bullet.collision.ContactListener
import com.jme3.bullet.collision.PhysicsCollisionObject

class GuardCollisionCallback: CollisionCallback {

    override fun onStarted(
        o1: PhysicsCollisionObject,
        o2: PhysicsCollisionObject,
        manifoldId: Long
    ) {

    }

    override fun onProcessed(
        o1: PhysicsCollisionObject,
        o2: PhysicsCollisionObject,
        manifoldId: Long
    ) {

    }

    override fun onEnded(
        o1: PhysicsCollisionObject,
        o2: PhysicsCollisionObject,
        manifoldId: Long
    ) {

    }


}