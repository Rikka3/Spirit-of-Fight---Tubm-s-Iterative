package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.resource.common.SparkResourcePathBuilder
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.poise_system.SOFHitTypes
import net.minecraft.resources.ResourceLocation

object SOFTypedAnimations {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val HIT_ANIMS = createHitAnim()

    @JvmStatic
    val PLAYER_HIT_LANDING = SpiritOfFight.REGISTER.typedAnimation()
        .id("hit_landing")
        .animIndex(AnimIndex(SparkResourcePathBuilder.buildResourcePath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "models", "player"), "Hit/landing"))
        .provider {
            EntityHitApplier.hitAnimDoFreeze(this)
        }
        .build()

    @JvmStatic
    val PARRIED_LEFT = SpiritOfFight.REGISTER.typedAnimation()
        .id("parried_left")
        .animIndex(AnimIndex(SparkResourcePathBuilder.buildResourcePath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "models", "player"), "common:parried_left"))
        .provider {
            EntityHitApplier.hitAnimDoFreeze(this)
        }
        .build()

    @JvmStatic
    val PARRIED_RIGHT = SpiritOfFight.REGISTER.typedAnimation()
        .id("parried_right")
        .animIndex(AnimIndex(SparkResourcePathBuilder.buildResourcePath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "models", "player"), "common:parried_right"))
        .provider {
            EntityHitApplier.hitAnimDoFreeze(this)
        }
        .build()

    fun createHitAnim(index: ResourceLocation = SparkResourcePathBuilder.buildResourcePath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "animations", "player/hit_animations")) =
        SOFHitTypes.HIT_ANIM_NAMES.mapValues { (animName, _) ->
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

}