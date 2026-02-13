package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.util.MoveDirection
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class InputDirectionCondition(
    val side: MoveDirection? = MoveDirection.FORWARD
): SkillTreeCondition {

    override fun test(
        player: Player,
        skill: Skill?
    ): Boolean {
        return player.isLocalPlayer && MoveDirection.getByInput((player as LocalPlayer).input) == side
    }

    override val description: Component
        get() {
            val d = (side?.translatableName ?: Component.translatable("move_direction.null")).withStyle(ChatFormatting.BOLD)
            return Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}", d)
        }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<InputDirectionCondition> = RecordCodecBuilder.mapCodec {
            it.group(MoveDirection.CODEC.optionalFieldOf("direction").forGetter { Optional.ofNullable(it.side) }).apply(it) { InputDirectionCondition(it.getOrNull()) }
        }
    }

}