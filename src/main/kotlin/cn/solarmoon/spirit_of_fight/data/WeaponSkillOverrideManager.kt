package cn.solarmoon.spirit_of_fight.data

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

/**
 * Manages weapon skill tree overrides.
 * Provides methods to query, add, and remove overrides.
 */
object WeaponSkillOverrideManager {

    /**
     * Gets the resolved skill trees for an item, checking both item and tag overrides.
     * Returns a list of all skill trees associated with the item.
     */
    fun getResolvedSkillTrees(item: Item): List<ResourceLocation> {
        val itemId = BuiltInRegistries.ITEM.getKey(item)
        val result = mutableListOf<ResourceLocation>()

        // Check item-specific override first
        WeaponSkillOverrideStorage.getItemOverride(itemId)?.let { result.addAll(it) }

        // Check tag overrides
        item.builtInRegistryHolder().tags().toList().forEach { tag ->
            WeaponSkillOverrideStorage.getTagOverride(tag.location)?.let { result.addAll(it) }
        }

        return result.distinct()
    }

    /**
     * Sets an override for a specific item.
     */
    fun setItemOverride(item: Item, skillTrees: List<ResourceLocation>) {
        val itemId = BuiltInRegistries.ITEM.getKey(item)
        WeaponSkillOverrideStorage.setItemOverride(itemId, skillTrees)
    }

    /**
     * Sets an override for an item tag.
     */
    fun setTagOverride(tagId: ResourceLocation, skillTrees: List<ResourceLocation>) {
        WeaponSkillOverrideStorage.setTagOverride(tagId, skillTrees)
    }

    /**
     * Removes an override for a specific item.
     */
    fun removeItemOverride(item: Item) {
        val itemId = BuiltInRegistries.ITEM.getKey(item)
        WeaponSkillOverrideStorage.removeItemOverride(itemId)
    }

    /**
     * Removes an override for an item tag.
     */
    fun removeTagOverride(tagId: ResourceLocation) {
        WeaponSkillOverrideStorage.removeTagOverride(tagId)
    }

    /**
     * Clears all overrides.
     */
    fun clearAll() {
        WeaponSkillOverrideStorage.clearAll()
    }

    /**
     * Gets all current overrides as a readable list.
     */
    fun getAllOverrides(): List<Pair<ResourceLocation, List<ResourceLocation>>> {
        return WeaponSkillOverrideStorage.getAllOverrides()
    }
}
