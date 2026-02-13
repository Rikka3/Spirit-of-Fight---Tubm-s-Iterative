package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.util.getName
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.Ingredient

class OffHandCondition(
    val ingredient: Ingredient
): SkillTreeCondition {

    override fun test(
        player: Player,
        skill: Skill?
    ): Boolean {
        return ingredient.test(player.offhandItem)
    }

    override val description: Component
        get() = Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}", ingredient.getName())

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<OffHandCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter { it.ingredient }
            ).apply(it, ::OffHandCondition)
        }
    }

}