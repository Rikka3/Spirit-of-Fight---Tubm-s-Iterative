package cn.solarmoon.spirit_of_fight.skill.component.sub.box

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.physics.host.PhysicsHost
import cn.solarmoon.spark_core.physics.presets.MoveWithAnimatedBoneTicker
import cn.solarmoon.spark_core.physics.toBVector3f
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.collision.shapes.BoxCollisionShape
import com.jme3.bullet.collision.shapes.CompoundCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.phys.Vec2
import org.joml.Vector3f
import java.util.UUID

class BoxFollowAnimatedBoneSubcomponent(
    val boneName: String,
    override val size: Vector3f,
    val offset: Vector3f,
    override val activeTime: List<Vec2>
): BoxGenerationSubcomponent {

    override val box = CompoundCollisionShape().apply { addChildShape(BoxCollisionShape(size.div(2f, Vector3f()).toBVector3f()), offset.toBVector3f()) }

    override fun createBody(owner: PhysicsHost): PhysicsRigidBody {
        return owner.bindBody(PhysicsRigidBody("${UUID.randomUUID()}", owner, box)) {
            isContactResponse = false
            setGravity(com.jme3.math.Vector3f.ZERO)
            addPhysicsTicker(MoveWithAnimatedBoneTicker(boneName))
            removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01)
        }
    }

    override val codec: MapCodec<out BoxGenerationSubcomponent> = CODEC

    companion object {
        val CODEC: MapCodec<BoxFollowAnimatedBoneSubcomponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("bone_name").forGetter { it.boneName },
                ExtraCodecs.VECTOR3F.fieldOf("size").forGetter { it.size },
                ExtraCodecs.VECTOR3F.fieldOf("offset").forGetter { it.offset },
                SerializeHelper.VEC2_CODEC.listOf().fieldOf("active_time").forGetter { it.activeTime }
            ).apply(it, ::BoxFollowAnimatedBoneSubcomponent)
        }
    }

}