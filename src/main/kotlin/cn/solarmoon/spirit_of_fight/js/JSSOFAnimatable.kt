package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.animation.anim.play.layer.BoneMask
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.js.extension.JSAnimatable
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spark_core.visual_effect.trail.TrailMesh
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.mozilla.javascript.NativeArray
import java.awt.Color
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

interface JSSOFAnimatable: JSAnimatable {

    fun summonTrail(mesh: TrailMesh, bone: String, off1: NativeArray, off2: NativeArray) {
        if (!js_animatable.animLevel.isClientSide) return
        mesh.addPoint(
            { js_animatable.getWorldBonePivot(bone, off1.toVec3(), it) },
            { js_animatable.getWorldBonePivot(bone, off2.toVec3(), it) },
        )
    }

    fun summonShadow(maxLifeTime: Int, color: Int) {
        val animatable = js_animatable
        if (animatable is Entity && !animatable.animLevel.isClientSide) {
            SparkVisualEffects.SHADOW.addToClient(animatable.id, maxLifeTime, Color(color))
        }
    }

    fun summonSpaceWarp(
        bone: String,
        offset: NativeArray,
        radius: Float,
        strength: Float,
        lifeTime: Int,
        hz: Float
    ) {
        val animatable = js_animatable
        if (animatable is Entity && !animatable.animLevel.isClientSide) {
            SparkVisualEffects.SPACE_WARP.addToClient(
                animatable.getWorldBonePivot(bone, offset.toVec3()).toVec3(),
                radius, strength, lifeTime, hz
            )
        }
    }

    fun blendMove() {
        val animatable = js_animatable
        if (animatable is Entity) {
            val value = if (animatable.isMoving) 0.0 else 1.0
            animatable.animController.getMainLayer().setBoneMask(BoneMask(mapOf("rightLeg" to value, "leftLeg" to value)))
        }
    }

    fun recoveryBlendMove() {
        val animatable = js_animatable
        animatable.animController.getMainLayer().setBoneMask(BoneMask(mapOf("rightLeg" to 1.0, "leftLeg" to 1.0)))
    }

    fun summonParticleSplash(
        bone: String,
        offset: NativeArray,
        radius: Float,       // 最终外圈半径
        strength: Float,     // 力度 → 粒子初速度
        rings: Int,          // 冲击波层数
        forwardBias: Float,  // 前冲比例
        density: Float       // 周向粒子密度系数（1.0 = 基准密度）
    ) {
        val animatable = js_animatable
        if (animatable !is Entity) return
        val level = animatable.level()

        // 获取两帧骨骼位置，计算法向量
        val p0 = animatable.getWorldBonePivot(bone, partialTick = 0f).toVec3()
        val p1 = animatable.getWorldBonePivot(bone, partialTick = 1f).toVec3()
        val baseCenter = animatable.getWorldBonePivot(bone, offset.toVec3(), 1f).toVec3()
        var dir = p1.subtract(p0).normalize()

        // 构建正交基
        var u = dir.cross(Vec3(0.0, 1.0, 0.0))
        if (u.lengthSqr() < 1e-6) u = dir.cross(Vec3(1.0, 0.0, 0.0))
        u = u.normalize()
        val v = dir.cross(u).normalize()

        val particle = ParticleTypes.CLOUD
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