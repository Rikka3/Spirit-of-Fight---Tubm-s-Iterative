package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.SkillTreeNode
import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

// This annotation tells NeoForge to look for @SubscribeEvent functions in this object
@EventBusSubscriber(modid = SpiritOfFight.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object SOFRegistries {

    // 1. Define the ResourceKeys
    val SKILL_TREE_CONDITION_CODEC_KEY: ResourceKey<Registry<MapCodec<out SkillTreeCondition>>> = 
        ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "skill_tree_condition_codec"))

    val SKILL_TREE_NODE_CODEC_KEY: ResourceKey<Registry<MapCodec<out SkillTreeNode>>> = 
        ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "skill_tree_node_codec"))

    // 2. Build the actual registries
    @JvmStatic
    val SKILL_TREE_CONDITION_CODEC: Registry<MapCodec<out SkillTreeCondition>> = 
        RegistryBuilder(SKILL_TREE_CONDITION_CODEC_KEY)
            .sync(true)
            .create()

    @JvmStatic
    val SKILL_TREE_NODE_CODEC: Registry<MapCodec<out SkillTreeNode>> = 
        RegistryBuilder(SKILL_TREE_NODE_CODEC_KEY)
            .sync(true)
            .create()

    @JvmStatic
    val SKILL_TREE: ResourceKey<Registry<SkillTree>> = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "skill_tree"))

    @SubscribeEvent
    @JvmStatic
    fun onNewRegistry(event: NewRegistryEvent) {
        event.register(SKILL_TREE_CONDITION_CODEC)
        event.register(SKILL_TREE_NODE_CODEC)
    }

    @JvmStatic
    fun register() {
        // Keeps the existing trigger function
    }
}