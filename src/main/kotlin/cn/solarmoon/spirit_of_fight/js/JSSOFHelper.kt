package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.js.JSComponent
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spark_core.visual_effect.trail.TrailInfo
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.phys.Vec3
import org.mozilla.javascript.Function
import java.awt.Color

object JSSOFHelper: JSComponent() {

    fun guard(damageSource: DamageSource, guardBody: PhysicsCollisionObject, onGuard: Function) {
        SkillHelper.guard(damageSource, guardBody) { a, b -> onGuard.call(engine, a, b) }
    }

    fun grabToBone(animatable: IEntityAnimatable<*>, bone: String, offset: Vec3) {
        SkillHelper.grabToBone(animatable, bone, offset)
    }

    fun createAnimByDirection(animatable: IEntityAnimatable<*>, animBaseName: String, default: String): AnimInstance {
        return SkillHelper.createAnimByDirection(animatable, animBaseName, MoveDirection.valueOf(default.uppercase()))
    }

    fun createTrailMesh(texture: String, lifeTime: Int, color: Int) = SparkVisualEffects.TRAIL.createMesh(TrailInfo(ResourceLocation.parse(texture), lifeTime, Color(color)))

}