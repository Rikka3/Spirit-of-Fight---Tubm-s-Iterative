package cn.solarmoon.spirit_of_fight.skill.component.sub.preinput

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.mojang.serialization.MapCodec
import java.util.function.Function

interface PreInputSubcomponent {

    val codec: MapCodec<out PreInputSubcomponent>

    companion object {
        val CODEC = SOFRegistries.SUBCOMPONENT_PREINPUT_CODEC.byNameCodec()
            .dispatch(
                PreInputSubcomponent::codec,
                Function.identity()
            )
    }

}