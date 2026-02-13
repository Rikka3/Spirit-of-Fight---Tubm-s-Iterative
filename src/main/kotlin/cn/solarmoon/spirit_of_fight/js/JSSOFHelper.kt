package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.js.JSComponent
import cn.solarmoon.spark_core.js.call
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spark_core.visual_effect.trail.TrailInfo
import cn.solarmoon.spirit_of_fight.util.OneTimeTrigger
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import org.mozilla.javascript.Function
import org.mozilla.javascript.NativeArray
import java.awt.Color

object JSSOFHelper: JSComponent() {

    fun guard(damageSource: DamageSource, guardBody: PhysicsCollisionObject, onGuard: Function) {
        SkillHelper.guard(damageSource, guardBody) { a, b -> onGuard.call(engine, a, b) }
    }

    fun grabToBone(animatable: IEntityAnimatable<*>, bone: String, offset: NativeArray) {
        SkillHelper.grabToBone(animatable, bone, offset.toVec3())
    }

    fun createAnimByDirection(animatable: IEntityAnimatable<*>, path: String, animBaseName: String, default: String): AnimInstance {
        return SkillHelper.createAnimByDirection(animatable, ResourceLocation.parse(path), animBaseName, MoveDirection.valueOf(default.uppercase()))
    }

    fun createTrailMesh(texture: String, lifeTime: Int, color: Int) = SparkVisualEffects.TRAIL.createMesh(TrailInfo(ResourceLocation.parse(texture), lifeTime, Color(color)))

    fun preventLocalInput(event: MovementInputUpdateEvent) {
        val player = event.entity as LocalPlayer
        event.input.apply {
            forwardImpulse = 0f
            leftImpulse = 0f
            up = false
            down = false
            left = false
            right = false
            jumping = false
            shiftKeyDown = false
            player.sprintTriggerTime = -1
            player.swinging = false
        }
    }

    fun preventLocalInputExceptMove(event: MovementInputUpdateEvent, moveLimit: Float) {
        val player = event.entity as LocalPlayer
        event.input.apply {
            forwardImpulse /= moveLimit
            leftImpulse /= moveLimit
            jumping = false
            shiftKeyDown = false
            player.sprintTriggerTime = -1
            player.swinging = false
            player.isSprinting = false
        }
    }

    fun createOneTimeTrigger(condition: Function, action: Function): OneTimeTrigger {
        return OneTimeTrigger(
            condition = { condition.call(engine) as Boolean },
            action = { action.call(engine) }
        )
    }

}