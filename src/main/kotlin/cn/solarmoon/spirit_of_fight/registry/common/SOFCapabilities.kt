package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import java.util.SortedSet
import java.util.TreeSet

object SOFCapabilities {

    @JvmStatic
    val SKILL_TREE = ItemCapability.create(
        ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "skill_tree"),
        SkillTreeSet::class.java,
        Level::class.java
    )

    private fun applyToItem(event: RegisterCapabilitiesEvent) {
        BuiltInRegistries.ITEM.forEach {
            event.registerItem(
                SKILL_TREE,
                { stack, level ->
                    val set = SkillTreeSet()
                    level.registryAccess().registry(SOFRegistries.SKILL_TREE).get().forEach { tree ->
                        if (tree.ingredient.test(stack)) set.add(tree)
                    }
                    if (set.isEmpty) null else set.get().apply { forEach { it.root = this } }
                },
                it
            )
        }
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::applyToItem)
    }

}