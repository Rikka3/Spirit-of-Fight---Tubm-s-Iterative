package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spirit_of_fight.skill.component.box.BoxGenerationType
import com.mojang.serialization.MapCodec

object SOFRegistries {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val BOX_GENERATION_TYPE_CODEC = SparkCore.REGISTER.registry<MapCodec<out BoxGenerationType>>()
        .id("box_generation_type_codec")
        .build { it.sync(true).create() }

}