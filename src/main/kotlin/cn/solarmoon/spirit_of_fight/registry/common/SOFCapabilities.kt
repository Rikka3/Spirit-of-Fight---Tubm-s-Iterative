package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.data.WeaponSkillOverrideManager
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

object SOFCapabilities {

    @JvmStatic
    val SKILL_TREE_CAPABILITY = ItemCapability.create(
        ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "skill_tree"),
        SkillTreeSet::class.java,
        Level::class.java
    )

    private fun applyToItem(event: RegisterCapabilitiesEvent) {
        BuiltInRegistries.ITEM.forEach { item ->
            event.registerItem(
                SKILL_TREE_CAPABILITY,
                { stack, level ->
                    val set = SkillTreeSet()
                    level.registryAccess().registry(SOFRegistries.SKILL_TREE).ifPresent { registry ->
                        // Check for weapon skill overrides
                        val overrideSkillTreeIds = WeaponSkillOverrideManager.getResolvedSkillTrees(item)

                        if (overrideSkillTreeIds.isNotEmpty()) {
                            // Use all overridden skill trees
                            overrideSkillTreeIds.forEach { skillTreeId ->
                                registry.get(skillTreeId)?.let { tree ->
                                    set.add(tree)
                                }
                            }
                            set.sortedByPriority().apply { forEach { it.root = this } }
                            return@ifPresent
                        }

                        // No override, use default ingredient matching
                        registry.forEach { tree: SkillTree ->
                            if (tree.ingredient.test(stack)) {
                                set.add(tree)
                            }
                        }
                    }
                    if (set.isEmpty()) {
                         null
                    } else {
                        set.sortedByPriority().apply { forEach { it.root = this } }
                    }
                },
                item
            )
        }
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::applyToItem)
    }

}
