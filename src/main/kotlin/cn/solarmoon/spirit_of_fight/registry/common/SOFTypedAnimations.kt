package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.TypedAnimProvider
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.hit.EntityHitApplier
import cn.solarmoon.spirit_of_fight.hit.SOFHitTypes
import net.minecraft.resources.ResourceLocation
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
    val HIT_ANIMS = createHitAnim()

    @JvmStatic
    val PLAYER_HIT_LANDING = SpiritOfFight.REGISTER.typedAnimation()
        .id("hit_landing")
        .animIndex(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "Hit/landing"))
        .provider {
            EntityHitApplier.hitAnimDoFreeze(this)
        }
        .build()

    fun createHitAnim(index: ResourceLocation = ResourceLocation.withDefaultNamespace("player")) = SOFHitTypes.HIT_ANIM_NAMES.mapValues { (animName, _) ->
        SpiritOfFight.REGISTER.typedAnimation()
            .id(animName
                .split("/").last()
                .replace(Regex("[^A-Za-z0-9]"), "_") // 替换所有非字母数字字符为下划线
                .lowercase())
            .animIndex(AnimIndex(index, animName))
            .provider {
                EntityHitApplier.hitAnimDoFreeze(this)
            }
            .build()
    }

    fun createStateAnim(name: String, index: ResourceLocation = ResourceLocation.withDefaultNamespace("player"), provider: TypedAnimProvider = {}) = SpiritOfFight.REGISTER.typedAnimation()
        .id(name)
        .animIndex(AnimIndex(index, "EntityState/$name"))
        .provider(provider)
        .build()

    fun createMoveStateAnim(name: String, index: ResourceLocation = ResourceLocation.withDefaultNamespace("player"), provider: TypedAnimProvider = {}) = createStateAnim(name, index) {
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