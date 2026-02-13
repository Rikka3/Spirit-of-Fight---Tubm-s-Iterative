package cn.solarmoon.spirit_of_fight.data

import net.minecraft.resources.ResourceLocation

/**
 * Stores weapon-to-skill-tree override mappings.
 * Mappings can target specific items or item tags.
 * Each target can have multiple skill trees associated with it.
 */
object WeaponSkillOverrideStorage {

    private val itemOverrides = mutableMapOf<ResourceLocation, List<ResourceLocation>>()
    private val tagOverrides = mutableMapOf<ResourceLocation, List<ResourceLocation>>()

    fun hasItemOverride(item: ResourceLocation): Boolean = itemOverrides.containsKey(item)
    fun getItemOverride(item: ResourceLocation): List<ResourceLocation>? = itemOverrides[item]

    fun hasTagOverride(tag: ResourceLocation): Boolean = tagOverrides.containsKey(tag)
    fun getTagOverride(tag: ResourceLocation): List<ResourceLocation>? = tagOverrides[tag]

    fun setItemOverride(item: ResourceLocation, skillTrees: List<ResourceLocation>) {
        itemOverrides[item] = skillTrees
    }

    fun setTagOverride(tag: ResourceLocation, skillTrees: List<ResourceLocation>) {
        tagOverrides[tag] = skillTrees
    }

    fun removeItemOverride(item: ResourceLocation) {
        itemOverrides.remove(item)
    }

    fun removeTagOverride(tag: ResourceLocation) {
        tagOverrides.remove(tag)
    }

    fun clearAll() {
        itemOverrides.clear()
        tagOverrides.clear()
    }

    fun getAllOverrides(): List<Pair<ResourceLocation, List<ResourceLocation>>> {
        return itemOverrides.entries.map { it.key to it.value } +
               tagOverrides.entries.map { it.key to it.value }
    }
}
