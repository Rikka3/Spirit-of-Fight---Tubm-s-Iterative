package cn.solarmoon.spirit_of_fight.skill.component.sub.box

import cn.solarmoon.spark_core.physics.host.PhysicsHost
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.jme3.bullet.collision.shapes.CollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import net.minecraft.world.phys.Vec2
import org.joml.Vector3f
import java.util.function.Function

interface BoxGenerationSubcomponent {

    val codec: MapCodec<out BoxGenerationSubcomponent>

    val activeTime: List<Vec2>

    val size: Vector3f

    val box: CollisionShape

    fun createBody(owner: PhysicsHost): PhysicsRigidBody

    companion object {
        val CODEC = SOFRegistries.SUBCOMPONENT_BOX_GENERATION_CODEC.byNameCodec()
            .dispatch(
                BoxGenerationSubcomponent::codec,
                Function.identity()
            )
    }

}