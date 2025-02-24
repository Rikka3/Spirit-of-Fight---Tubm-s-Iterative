package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.TypedAnimProvider
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes

object SOFTypedAnimations {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val HAMMER_IDLE = createStateAnim("hammer_idle")
    @JvmStatic
    val HAMMER_WALK = createMoveStateAnim("hammer_walk")
    @JvmStatic
    val HAMMER_WALK_BACK = createMoveStateAnim("hammer_walk_back")
    @JvmStatic
    val HAMMER_SPRINTING = createMoveStateAnim("hammer_sprinting")
    @JvmStatic
    val HAMMER_FALL = createStateAnim("hammer_fall")
    @JvmStatic
    val BAIMEI_IDLE = createStateAnim("baimei_idle")
    @JvmStatic
    val BAIMEI_WALK = createMoveStateAnim("baimei_walk")
    @JvmStatic
    val BAIMEI_WALK_BACK = createMoveStateAnim("baimei_walk_back")
    @JvmStatic
    val BAIMEI_SPRINTING = createMoveStateAnim("baimei_sprinting")
    @JvmStatic
    val SHIELD_SPRINTING = createMoveStateAnim("shield_sprinting")

    @JvmStatic
    val HIT_LANDING = SpiritOfFight.REGISTER.typedAnimation()
        .id("hit_landing")
        .animName("Hit/landing")
        .provider {
            onEvent<AnimEvent.SwitchIn> {
                val entity = holder.animatable as? Entity ?: return@onEvent
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, true)
                entity.putFlag(SparkFlags.DISARM, true)
                entity.putFlag(SparkFlags.SILENCE, true)
            }

            onEvent<AnimEvent.End> {
                val entity = holder.animatable as? Entity ?: return@onEvent
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, false)
                entity.putFlag(SparkFlags.DISARM, false)
                entity.putFlag(SparkFlags.SILENCE, false)
            }
        }
        .build()

    fun createStateAnim(name: String, provider: TypedAnimProvider = {}) = SpiritOfFight.REGISTER.typedAnimation()
        .id(name)
        .animName("EntityState/$name")
        .provider(provider)
        .build()

    fun createMoveStateAnim(name: String, provider: TypedAnimProvider = {}) = createStateAnim(name) {
        onEvent<AnimEvent.Tick> {
            val holder = this.holder
            if (holder is LivingEntity) {
                speed = holder.getAttributeValue(Attributes.MOVEMENT_SPEED) / (if (holder.isSprinting) 0.13 else 0.1)
                if (holder.isUsingItem) speed /= 1.5
            }
        }
        provider.invoke(this)
    }

}