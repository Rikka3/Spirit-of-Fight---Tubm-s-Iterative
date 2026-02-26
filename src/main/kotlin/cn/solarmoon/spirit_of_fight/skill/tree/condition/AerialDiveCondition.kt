package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.phys.AABB
import org.joml.Vector3f
import kotlin.math.acos

/**
 * Condition that checks if player can perform an aerial dive attack.
 * Requirements:
 * - Player is falling (not on ground, has fall distance)
 * - Player is holding a mace
 * - There is a target entity within range and in front of the player
 */
class AerialDiveCondition(
    val range: Double = 10.0,
    val minFallDistance: Float = 1.5f,
    val coneAngleDegrees: Double = 60.0
) : SkillTreeCondition {

    override fun test(player: Player, skill: Skill?): Boolean {
        // Must be falling (not on ground, has fall distance)
        if (player.onGround()) return false
        if (player.fallDistance < minFallDistance) return false

        // Must be holding a mace
        val mainHandItem = player.mainHandItem
        if (!mainHandItem.`is`(Items.MACE)) return false

        // Find target entity within range and cone
        val target = findTargetEntity(player) ?: return false

        // Store target and direction in entity patch
        val patch = (player as? IEntityPatch) ?: return false

        // Calculate direction to target
        val playerPos = player.position()
        val targetPos = target.position()
        val direction = Vector3f(
            (targetPos.x - playerPos.x).toFloat(),
            (targetPos.y - playerPos.y).toFloat(),
            (targetPos.z - playerPos.z).toFloat()
        ).normalize()

        // Store in patch for skill script to use
        patch.aerialDiveTarget = target.id
        patch.aerialDiveDirection = direction

        return true
    }

    private fun findTargetEntity(player: Player): Entity? {
        val playerPos = player.position()
        val lookVec = player.lookAngle

        // Get all entities within range
        val aabb = AABB.ofSize(playerPos, range * 2, range * 2, range * 2)
        val entities = player.level().getEntities(player, aabb) { entity ->
            entity != player && entity.isAlive && entity.isPickable
        }

        // Find closest entity within cone
        var closestEntity: Entity? = null
        var closestDistance: Double = range + 1

        val coneRad = Math.toRadians(coneAngleDegrees)

        for (entity in entities) {
            val distance = player.distanceTo(entity).toDouble()
            if (distance > range) continue

            // Check if entity is within cone
            val toEntity = entity.position().subtract(playerPos).normalize()
            val dot = lookVec.dot(toEntity)
            val angle = acos(dot)

            if (angle <= coneRad && distance < closestDistance) {
                closestEntity = entity
                closestDistance = distance
            }
        }

        return closestEntity
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<AerialDiveCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.DOUBLE.optionalFieldOf("range", 10.0).forGetter { it.range },
                Codec.FLOAT.optionalFieldOf("min_fall_distance", 1.5f).forGetter { it.minFallDistance },
                Codec.DOUBLE.optionalFieldOf("cone_angle", 60.0).forGetter { it.coneAngleDegrees }
            ).apply(it, ::AerialDiveCondition)
        }
    }
}
