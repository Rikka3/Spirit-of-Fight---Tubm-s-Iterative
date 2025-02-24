package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.registry.common.SparkSkillContext
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class HitTargetCondition(): SkillTreeCondition {

    override fun test(host: Player, skill: Skill?): Boolean {
        return skill != null && skill.blackBoard.read(SparkSkillContext.ENTITY_TARGET) != null
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<HitTargetCondition> = RecordCodecBuilder.mapCodec {
            it.stable(HitTargetCondition())
        }
    }

}