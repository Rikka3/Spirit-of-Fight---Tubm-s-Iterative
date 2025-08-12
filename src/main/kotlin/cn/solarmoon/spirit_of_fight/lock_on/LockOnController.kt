package cn.solarmoon.spirit_of_fight.lock_on

import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

object LockOnController {

    private var TARGET: Entity? = null

    val lookDistance = 64.0

    @JvmStatic
    val target get() = TARGET

    @JvmStatic
    val hasTarget get() = TARGET != null

    @JvmStatic
    fun getLookPos(partialTicks: Float = 1f): Vec3 {
        val target = target
        if (target != null) {
            val center = target.getPosition(partialTicks)
            val maxY = target.boundingBox.maxY - target.boundingBox.minY
            return Vec3(center.x, center.y + maxY / 4, center.z)
        }
        return Vec3.ZERO
    }

    @JvmStatic
    fun setTarget(entity: Entity?) {
        TARGET = entity
    }

    @JvmStatic
    fun clear() {
        setTarget(null)
    }

}