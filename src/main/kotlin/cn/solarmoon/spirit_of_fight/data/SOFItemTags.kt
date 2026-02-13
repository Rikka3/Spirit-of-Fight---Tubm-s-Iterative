package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class SOFItemTags(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper
): ItemTagsProvider(output, lookupProvider, blockTags, SpiritOfFight.MOD_ID, existingFileHelper) {

    override fun addTags(provider: HolderLookup.Provider) {
        tag(FORGE_HAMMERS).add(
            SOFItems.WOODEN_WARHAMMER.get(),
            SOFItems.STONE_WARHAMMER.get(),
            SOFItems.IRON_WARHAMMER.get(),
            SOFItems.GOLDEN_WARHAMMER.get(),
            SOFItems.DIAMOND_WARHAMMER.get(),
            SOFItems.NETHERITE_WARHAMMER.get()
        )
        tag(FORGE_GLOVES).add(
            SOFItems.LEATHER_GLOVES.get(),
            SOFItems.IRON_GLOVES.get(),
            SOFItems.GOLDEN_GLOVES.get(),
            SOFItems.DIAMOND_GLOVES.get(),
            SOFItems.NETHERITE_GLOVES.get()
        )
        tag(FORGE_SPEARS).add(
            SOFItems.WOODEN_SPEAR.get(),
            SOFItems.STONE_SPEAR.get(),
            SOFItems.IRON_SPEAR.get(),
            SOFItems.GOLDEN_SPEAR.get(),
            SOFItems.DIAMOND_SPEAR.get(),
            SOFItems.NETHERITE_SPEAR.get()
        )
        tag(FORGE_AXES).add(
            Items.WOODEN_AXE,
            Items.STONE_AXE,
            Items.IRON_AXE,
            Items.GOLDEN_AXE,
            Items.DIAMOND_AXE,
            Items.NETHERITE_AXE,
            SOFItems.WOODEN_BATTLEAXE.get(),
            SOFItems.STONE_BATTLEAXE.get(),
            SOFItems.IRON_BATTLEAXE.get(),
            SOFItems.GOLDEN_BATTLEAXE.get(),
            SOFItems.DIAMOND_BATTLEAXE.get(),
            SOFItems.NETHERITE_BATTLEAXE.get()
        )
        tag(FORGE_GREATSWORDS).add(
            SOFItems.WOODEN_GREATSWORD.get(),
            SOFItems.STONE_GREATSWORD.get(),
            SOFItems.IRON_GREATSWORD.get(),
            SOFItems.GOLDEN_GREATSWORD.get(),
            SOFItems.DIAMOND_GREATSWORD.get(),
            SOFItems.NETHERITE_GREATSWORD.get()
        )
    }

    companion object {
        @JvmStatic
        val FORGE_HAMMERS = forgeTag("hammers")
        @JvmStatic
        val FORGE_GLOVES = forgeTag("gloves")
        @JvmStatic
        val FORGE_SPEARS = forgeTag("spears")
        @JvmStatic
        val FORGE_AXES = forgeTag("axes")
        @JvmStatic
        val FORGE_GREATSWORDS = forgeTag("greatswords")

        private fun modTag(path: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, path))
        }

        private fun forgeTag(path: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path))
        }
    }

}