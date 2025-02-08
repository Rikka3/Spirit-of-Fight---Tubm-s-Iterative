package cn.solarmoon.spirit_of_fight.skill.component.sub.preinput

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.entity.preinput.PreInput
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.phys.Vec2

class PreInputReleaseSubcomponent(
    val nodes: List<Vec2>,
    val conditionList: Either<List<String>, List<String>>
): PreInputSubcomponent {

    override val codec: MapCodec<out PreInputSubcomponent> = CODEC

    fun release(preInput: PreInput, time: Double) {
        val whitelist = conditionList.left()
        val blacklist = conditionList.right()

        nodes.forEach {
            if (time in it.x..it.y) {
                if (whitelist.isPresent) {
                    whitelist.get().forEach {
                        preInput.executeIfPresent(it)
                    }
                    return
                }
                else if (blacklist.isPresent) {
                    if (preInput.id !in blacklist.get()) preInput.executeIfPresent()
                }
            }
        }
    }

    companion object {
        val CODEC: MapCodec<PreInputReleaseSubcomponent> = RecordCodecBuilder.mapCodec {
            it.group(
                SerializeHelper.VEC2_CODEC.listOf().fieldOf("nodes").forGetter { it.nodes },
                Codec.either(Codec.STRING.listOf().fieldOf("whitelist").codec(), Codec.STRING.listOf().fieldOf("blacklist").codec()).optionalFieldOf("condition", Either.right(listOf())).forGetter { it.conditionList }
            ).apply(it, ::PreInputReleaseSubcomponent)
        }
    }
}