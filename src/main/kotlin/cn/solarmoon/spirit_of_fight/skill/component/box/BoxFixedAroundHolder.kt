package cn.solarmoon.spirit_of_fight.skill.component.box

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.physics.host.PhysicsHost
import cn.solarmoon.spark_core.physics.presets.MoveWithAnimatedBoneTicker
import cn.solarmoon.spark_core.physics.presets.RotateAroundHostTicker
import cn.solarmoon.spark_core.physics.toBVector3f
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.collision.shapes.BoxCollisionShape
import com.jme3.bullet.collision.shapes.CollisionShape
import com.jme3.bullet.collision.shapes.CompoundCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.phys.Vec2
import org.apache.commons.lang3.DoubleRange
import org.joml.Vector3f
import java.util.UUID

class BoxFixedAroundHolder(
    override val size: Vector3f,
    val offset: Vector3f,
    val rot: Float,
    val radius: Float,
    override val activeTime: List<Vec2>
): BoxGenerationType {

    override val box: CollisionShape = CompoundCollisionShape().apply {
        addChildShape(BoxCollisionShape(size.div(2f).toBVector3f()), offset.toBVector3f())
    }

    override fun createBody(owner: PhysicsHost): PhysicsRigidBody {
        return owner.bindBody(PhysicsRigidBody("${UUID.randomUUID()}", owner, box)) {
            isContactResponse = false
            setGravity(com.jme3.math.Vector3f.ZERO)
            addPhysicsTicker(RotateAroundHostTicker())
        }
    }

    override val codec: MapCodec<out BoxGenerationType> = CODEC

    companion object {
        val CODEC: MapCodec<BoxFixedAroundHolder> = RecordCodecBuilder.mapCodec {
            it.group(
                ExtraCodecs.VECTOR3F.fieldOf("size").forGetter { it.size },
                ExtraCodecs.VECTOR3F.fieldOf("offset").forGetter { it.offset },
                Codec.FLOAT.fieldOf("rot").forGetter { it.rot },
                Codec.FLOAT.fieldOf("radius").forGetter { it.radius },
                SerializeHelper.VEC2_CODEC.listOf().fieldOf("active_time").forGetter { it.activeTime }
            ).apply(it, ::BoxFixedAroundHolder)
        }
    }

}