package cn.solarmoon.spirit_of_fight.skill.component.box

import cn.solarmoon.spark_core.physics.host.PhysicsHost
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.jme3.bullet.collision.shapes.CollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec2
import org.joml.Vector3f
import java.util.function.Function

interface BoxGenerationType {

    val activeTime: List<Vec2>

    val size: Vector3f

    val box: CollisionShape

    fun createBody(owner: PhysicsHost): PhysicsRigidBody

    val registryKey: ResourceLocation get() = SOFRegistries.BOX_GENERATION_TYPE_CODEC.getKey(codec) ?: throw NullPointerException("碰撞箱生成类型尚未注册")

    val codec: MapCodec<out BoxGenerationType>

    companion object {
        val CODEC = SOFRegistries.BOX_GENERATION_TYPE_CODEC.byNameCodec()
            .dispatch(
                BoxGenerationType::codec,
                Function.identity()
            )
    }

}