package cn.solarmoon.spirit_of_fight.fighter.player

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.physics.presets.MoveWithAnimatedBoneTicker
import cn.solarmoon.spark_core.physics.presets.initWithAnimatedBone
import cn.solarmoon.spark_core.physics.toRadians
import com.jme3.bullet.collision.shapes.CompoundCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3d
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PlayerBodyApplier {

    @SubscribeEvent
    private fun playerJoinLevel(event: EntityJoinLevelEvent) {
        val entity = event.entity
        if (entity is Player && entity is IEntityAnimatable<*>) {
            entity.model.bones.values.filterNot { it.name in listOf("rightItem", "leftItem") }.forEach {
                val b = PhysicsRigidBody(it.name, entity, CompoundCollisionShape())
                entity.bindBody(b, event.level.physicsLevel, true) {
                    isContactResponse = false
                    setGravity(Vector3f.ZERO)
                    addPhysicsTicker(MoveWithAnimatedBoneTicker(it.name))
                    (collisionShape as CompoundCollisionShape).initWithAnimatedBone(it)
                }
            }
        }
    }

}