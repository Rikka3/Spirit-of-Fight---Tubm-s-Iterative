package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spirit_of_fight.skill.component.sub.box.BoxGenerationSubcomponent
import cn.solarmoon.spirit_of_fight.skill.component.sub.preinput.PreInputSubcomponent
import com.mojang.serialization.MapCodec

object SOFRegistries {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val SUBCOMPONENT_BOX_GENERATION_CODEC = SparkCore.REGISTER.registry<MapCodec<out BoxGenerationSubcomponent>>()
        .id("subcomponent_box_generation_codec")
        .build { it.sync(true).create() }

    @JvmStatic
    val SUBCOMPONENT_PREINPUT_CODEC = SparkCore.REGISTER.registry<MapCodec<out PreInputSubcomponent>>()
        .id("subcomponent_preinput_codec")
        .build { it.sync(true).create() }

}