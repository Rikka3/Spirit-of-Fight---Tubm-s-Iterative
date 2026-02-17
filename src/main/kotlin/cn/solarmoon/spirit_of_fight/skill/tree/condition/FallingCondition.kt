package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.player.Player

class FallingCondition: SkillTreeCondition {

    override fun test(
        player: Player,
        skill: Skill?
    ): Boolean {
        // Check if player is in air (not on ground) - triggers immediately when leaving ground
        return !player.onGround()
    }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<FallingCondition> = MapCodec.unit(FallingCondition())
    }

}
