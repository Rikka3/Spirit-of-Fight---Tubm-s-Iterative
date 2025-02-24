package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.skill.Key
import com.jme3.bullet.objects.PhysicsRigidBody
import net.minecraft.world.phys.Vec3

object SOFSkillContext {

    val FIGHT_SPIRIT_MULTIPLY = Key.create<Double>("fight_spirit_multiply")
    val PARTICLE_POSITION = Key.create<Vec3>("particle_position")
    val RIGID_BODIES = Key.create<List<PhysicsRigidBody>>("rigid_bodies")

}