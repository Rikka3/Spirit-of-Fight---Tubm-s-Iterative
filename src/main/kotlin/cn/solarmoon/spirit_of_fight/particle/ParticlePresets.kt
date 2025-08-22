package cn.solarmoon.spirit_of_fight.particle

import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getRelativeVector
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spirit_of_fight.particle.sync.QuadraticParticlePayload

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

class ParticlePresets(
    val level: Level,
    val sync: Boolean
) {

    fun summonQuadraticParticle(pos: Vec3, amount: Int, particle: ParticleOptions) {
        if (sync) {
            PacketDistributor.sendToAllPlayers(QuadraticParticlePayload(amount, pos, particle))
        } else {
            repeat(amount) {
                level.addParticle(particle, pos.x, pos.y, pos.z, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5, Random.nextDouble() - 0.5)
            }
        }
    }

    fun summonSeismicWave(entity: Entity, totalParticles: Int, speed: Double, offset: Vec3, particleType: ParticleOptions) {
        val random = java.util.Random()
        val center = entity.position()
        val baseOffset = entity.getRelativeVector(offset)

        repeat(totalParticles) { i ->
            // 在角度和半径上加随机扰动
            val angle = (i * 2 * Math.PI / totalParticles) + (random.nextDouble() - 0.5) * 0.3
            val radiusJitter = (random.nextDouble() - 0.5) * 0.2
            val heightOffset = (random.nextDouble() - 0.5) * 0.1

            val particleX = center.x + baseOffset.x + (0.5 + radiusJitter) * Math.cos(angle)
            val particleY = center.y + 0.05 + baseOffset.y + heightOffset
            val particleZ = center.z + baseOffset.z + (0.5 + radiusJitter) * Math.sin(angle)

            // 速度也加点随机，让粒子方向不完全一致
            val speedX = (speed + (random.nextDouble() - 0.5) * 0.05) * Math.cos(angle)
            val speedY = 0.02 + (random.nextDouble() - 0.5) * 0.02
            val speedZ = (speed + (random.nextDouble() - 0.5) * 0.05) * Math.sin(angle)

            entity.level().addParticle(
                particleType,
                particleX, particleY, particleZ,
                speedX, speedY, speedZ
            )
        }
    }


    fun summonChargingParticle(center: Vec3, amount: Int, radius: Double, still: Boolean, particleType: ParticleOptions) {
        repeat(amount) {
            val angle = (it * 2 * Math.PI / amount)
            val particleX = center.x + radius * cos(angle)
            val particleY = center.y + 0.025
            val particleZ = center.z + radius * sin(angle)
            val speedX = if (!still) (center.x - particleX) * 0.1 else 0.0
            val speedY = 0.0
            val speedZ = if (!still) (center.z - particleZ) * 0.1 else 0.0
            level.addParticle(particleType, particleX, particleY, particleZ, speedX, speedY, speedZ)
        }
    }

    fun summonSplashRelateYRot(
        animatable: IEntityAnimatable<*>,
        particle: ParticleOptions,
        bone: String,
        relateV: Vec3,
        offset: Vec3,
        radius: Float,
        strength: Float,
        rings: Int,
        forwardBias: Float,
        density: Float
    ) {
        // 将相对向量（基于实体朝向的局部坐标系）转成世界方向
        val worldDir = animatable.animatable.getRelativeVector(relateV).normalize()

        summonSplashAtBone(
            animatable,
            particle,
            bone,
            offset,
            radius,
            strength,
            rings,
            forwardBias,
            density,
            worldDir
        )
    }


    fun summonSplashFromBoneMotion(
        animatable: IAnimatable<*>,
        particle: ParticleOptions,
        bone: String,
        offset: Vec3,
        radius: Float,
        strength: Float,
        rings: Int,
        forwardBias: Float,
        density: Float
    ) {
        // 获取两帧骨骼位置
        val p0 = animatable.getWorldBonePivot(bone, partialTick = 0f).toVec3()
        val p1 = animatable.getWorldBonePivot(bone, partialTick = 1f).toVec3()

        // 方向向量 = 下一帧 - 当前帧
        var dir = p1.subtract(p0).normalize()

        // 避免方向是零向量（静止或纯Y移动）
        if (dir.lengthSqr() < 1e-6) {
            dir = Vec3(0.0, 0.0, 1.0) // 兜底为世界Z+
        }

        // 调用基础版本
        summonSplashAtBone(
            animatable, particle, bone, offset,
            radius, strength, rings, forwardBias, density,
            dir // 这里传的就是刚算出来的方向
        )
    }

    fun summonSplashAtBone(
        animatable: IAnimatable<*>,
        particle: ParticleOptions,
        bone: String,
        offset: Vec3,
        radius: Float,       // 🌊 最终外圈半径（粒子波扩散到的最大距离）
        strength: Float,     // 💥 力度：粒子初速度的基准值（越大飞得越快）
        rings: Int,          // 🔄 冲击波层数：环形波的层数（从内到外分几圈）
        forwardBias: Float,  // ⏩ 前冲比例：粒子沿前进方向的速度占比（>1 前冲更明显）
        density: Float,      // 🔹 周向粒子密度系数（1.0 = 基准，>1 更密集，<1 更稀疏）
        worldDir: Vec3       // 🌐 绝对世界方向，例如 Vec3(0.0, 0.0, 1.0) 代表世界 Z+
    ) {
        val baseCenter = animatable.getWorldBonePivot(bone, offset, 1f).toVec3()

        // 确保方向是单位向量
        var dir = worldDir.normalize()
        if (dir.lengthSqr() < 1e-6) {
            dir = Vec3(0.0, 0.0, 1.0) // 避免零向量导致计算崩
        }

        // 正交基生成
        var u = dir.cross(Vec3(0.0, 1.0, 0.0))
        if (u.lengthSqr() < 1e-6) u = dir.cross(Vec3(1.0, 0.0, 0.0))
        u = u.normalize()
        val v = dir.cross(u).normalize()

        val basePoints = (24 + radius * 8f).roundToInt().coerceIn(12, 96)
        val rMin = 0.12
        val coneLength = radius.toDouble() * (0.8 + 0.6 * forwardBias.coerceIn(0f, 2f))

        for (ring in 0 until rings) {
            val t = (ring + 1).toDouble() / rings.toDouble()
            val ringCenter = baseCenter.add(dir.scale(coneLength * t))
            val r = rMin + (radius.toDouble() - rMin) * t

            val ringPoints = (basePoints * density * (0.7 + 0.3 * t))
                .roundToInt()
                .coerceIn(8, 160)

            val radialSpeed = strength.toDouble() * (0.8 + 0.4 * t)
            val forwardSpeed = strength.toDouble() * forwardBias.toDouble()

            for (i in 0 until ringPoints) {
                val theta = (2.0 * Math.PI * i) / ringPoints
                val radial = u.scale(cos(theta)).add(v.scale(sin(theta)))
                val pos = ringCenter.add(radial.scale(r))
                val vel = radial.scale(radialSpeed).add(dir.scale(forwardSpeed))

                level.addParticle(
                    particle,
                    pos.x, pos.y, pos.z,
                    vel.x, vel.y, vel.z
                )
            }
        }
    }


}