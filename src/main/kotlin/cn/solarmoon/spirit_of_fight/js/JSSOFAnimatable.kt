package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.animation.anim.play.layer.BoneMask
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.js.extension.JSAnimatable
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spark_core.visual_effect.trail.TrailMesh
import net.minecraft.world.entity.Entity
import org.mozilla.javascript.NativeArray
import java.awt.Color

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
            if (!animatable.persistentData.getBoolean("move_blending") && animatable.isMoving) {
                animatable.animController.getMainLayer().setBoneMask(BoneMask(mapOf("rightLeg" to 0.25, "leftLeg" to 0.25)))
                animatable.persistentData.putBoolean("move_blending", true)
            }
        }
    }

    fun recoveryBlendMove() {
        val animatable = js_animatable
        if (animatable is Entity) {
            animatable.animController.getMainLayer().setBoneMask(BoneMask(mapOf("rightLeg" to 1.0, "leftLeg" to 1.0)))
            animatable.persistentData.putBoolean("move_blending", false)
        }
    }

}