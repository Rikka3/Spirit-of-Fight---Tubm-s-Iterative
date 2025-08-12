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
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class SOFItemTags(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper
): ItemTagsProvider(output, lookupProvider, blockTags, SpiritOfFight.MOD_ID, existingFileHelper) {

    override fun addTags(provider: HolderLookup.Provider) {
        tag(FORGE_HAMMER).add(
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
    }

    companion object {
        @JvmStatic
        val FORGE_HAMMER = forgeTag("hammer")
        @JvmStatic
        val FORGE_GLOVES = forgeTag("gloves")
        @JvmStatic
        val FORGE_SPEARS = forgeTag("spears")

        private fun modTag(path: String): TagKey<Item> {
            Tags.Items.BUDS
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, path))
        }

        private fun forgeTag(path: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path))
        }
    }

}