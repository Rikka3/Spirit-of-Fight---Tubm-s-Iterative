package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class HitTargetCondition: SkillTreeCondition {

    override fun test(host: Player, skill: Skill?): Boolean {
        SparkCore.LOGGER.info("${skill != null && skill.targetPool.getTargets().isNotEmpty()}")
        return skill != null && skill.targetPool.getTargets().isNotEmpty()
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<HitTargetCondition> = RecordCodecBuilder.mapCodec {
            it.group(Codec.EMPTY.forGetter { Unit.INSTANCE }).apply(it) { HitTargetCondition() }
        }
    }

}