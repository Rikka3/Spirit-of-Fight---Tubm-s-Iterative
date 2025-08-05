package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.blend.BlendAnimation
import cn.solarmoon.spark_core.animation.anim.play.blend.BlendData
import cn.solarmoon.spark_core.animation.anim.play.blend.BlendMask
import cn.solarmoon.spark_core.js.extension.JSEntity
import cn.solarmoon.spark_core.resource.common.SparkResourcePathBuilder
import cn.solarmoon.spark_core.visual_effect.trail.TrailMesh
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import net.minecraft.world.phys.Vec3

interface JSSOFEntity: JSEntity {

    fun addFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.addStage(value)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun removeFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.removeStage(value)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun getFightSpirit() = entity.getFightSpirit()

    fun clearGrabs() = entity.grabManager.clear()

    fun blendMove() {
        val entity = entity
        if (entity !is IEntityAnimatable<*>) return

        val animPath = SparkResourcePathBuilder.buildAnimationPath(SparkCore.MOD_ID, SparkCore.MOD_ID, "player", "state.land.move")
        val blend = BlendAnimation(
            entity.createAnimation(animPath.toString()),
            BlendData(weight = 1000.0, blendMask = BlendMask(mapOf("rightLeg" to 1.0, "leftLeg" to 1.0), 0.0), refreshIfExist = false)
        )

        if (entity.isMoving) {
            entity.animController.blendAnimation(blend)
        } else entity.animController.removeBlend(blend.id)
    }

    fun toggleWieldStyle() {
        WieldStyle.switch(entity)
    }

    fun summonTrail(mesh: TrailMesh, bone: String, off1: Vec3, off2: Vec3) {
        val entity = entity
        if (entity !is IEntityAnimatable<*> || !entity.level().isClientSide) return
        mesh.addPoint(
            { entity.getWorldBonePivot(bone, off1, it) },
            { entity.getWorldBonePivot(bone, off2, it) },
        )
    }

}