package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass

object SOFRegistries {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val SKILL_TREE_CONDITION_CODEC = SpiritOfFight.REGISTER.registry<MapCodec<out SkillTreeCondition>>()
        .id("skill_tree_condition_codec")
        .valueType(MapCodec::class as KClass<out MapCodec<out SkillTreeCondition>>)
        .build { it.sync(true).create() }

    @JvmStatic
    val SKILL_TREE_NODE_CODEC = SpiritOfFight.REGISTER.registry<MapCodec<out SkillTreeNode>>()
        .id("skill_tree_node_codec")
        .valueType(MapCodec::class as KClass<out MapCodec<out SkillTreeNode>>)
        .build { it.sync(true).create() }

    @JvmStatic
    val SKILL_TREE = ResourceKey.createRegistryKey<SkillTree>(ResourceLocation.fromNamespaceAndPath("skill", "tree"))

}