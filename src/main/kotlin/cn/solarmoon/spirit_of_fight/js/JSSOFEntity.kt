package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.BlendAnimation
import cn.solarmoon.spark_core.entity.moveCheck
import cn.solarmoon.spark_core.js.extension.JSEntity
import cn.solarmoon.spark_core.registry.common.SparkTypedAnimations
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Pose

interface JSSOFEntity: JSEntity {

    fun addFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.addStage(value)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun getFightSpirit() = entity.getFightSpirit()

    fun clearGrabs() = entity.grabManager.clear()

    fun blendMove(enable: Boolean) {
        val entity = entity
        if (entity is IEntityAnimatable<*>) {
            if (enable) {
                entity.animController.blendSpace.putIfAbsent("#move", BlendAnimation(SparkTypedAnimations.WALK.get().create(entity), 5.0, entity.model.bones.keys.filter { it !in listOf("rightLeg", "leftLeg") }))
            } else {
                entity.animController.blendSpace.remove("#move")
            }
        }
    }

}