package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.skill.SkillGroupController
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation

class PreInputCommonReleaseController: SkillGroupController {

    override val skills: List<ResourceLocation> = listOf()

    override val codec: MapCodec<out SkillGroupController> = CODEC

    companion object {
        val CODEC: MapCodec<PreInputCommonReleaseController> = RecordCodecBuilder.mapCodec {
            it.stable(PreInputCommonReleaseController())
        }
    }
}