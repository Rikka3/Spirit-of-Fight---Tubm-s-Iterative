package cn.solarmoon.spirit_of_fight.entity.player

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.physics.presets.initWithAnimatedBone
import cn.solarmoon.spark_core.physics.presets.ticker.MoveWithAnimatedBoneTicker
import com.jme3.bullet.collision.shapes.CompoundCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent

object PlayerBodyApplier {

    @SubscribeEvent
    private fun playerJoinLevel(event: EntityJoinLevelEvent) {

    }

}