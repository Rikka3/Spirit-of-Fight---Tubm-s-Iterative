package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

class SkillEndCondition: SkillTreeCondition {

    override fun test(
        host: Player,
        skill: Skill?
    ): Boolean {
        return skill?.isActive == false
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<SkillEndCondition> = RecordCodecBuilder.mapCodec {
            it.stable(SkillEndCondition())
        }
    }

}