package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.util.MultiModuleResourceExtractionUtil
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
        .animIndex(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "hit.landing"))
        .provider {
            EntityHitApplier.hitAnimDoFreeze(this)
        }
        .build()

    fun createHitAnim(index: ResourceLocation = ResourceLocation.withDefaultNamespace("player")) =
        SOFHitTypes.HIT_ANIM_NAMES.mapValues { (animName, _) ->
            SpiritOfFight.REGISTER.typedAnimation()
                .id(MultiModuleResourceExtractionUtil.normalizeResourceName(animName))
                .animIndex(AnimIndex(index, MultiModuleResourceExtractionUtil.normalizeResourceName(animName)))
                .provider {
                    EntityHitApplier.hitAnimDoFreeze(this)
                }
                .build()
        }

}