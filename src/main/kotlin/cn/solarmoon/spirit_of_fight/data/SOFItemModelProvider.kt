package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredHolder

class SOFItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
): ItemModelProvider(output, SpiritOfFight.MOD_ID, existingFileHelper) {

    override fun registerModels() {
        inHandItemModel(SOFItems.WOODEN_WARHAMMER, "warhammer")
        inHandItemModel(SOFItems.STONE_WARHAMMER, "warhammer")
        inHandItemModel(SOFItems.IRON_WARHAMMER, "warhammer")
        inHandItemModel(SOFItems.GOLDEN_WARHAMMER, "warhammer")
        inHandItemModel(SOFItems.DIAMOND_WARHAMMER, "warhammer")
        inHandItemModel(SOFItems.NETHERITE_WARHAMMER, "warhammer")

        inHandItemModel(SOFItems.LEATHER_GLOVES, "gloves")
        inHandItemModel(SOFItems.IRON_GLOVES, "gloves")
        inHandItemModel(SOFItems.GOLDEN_GLOVES, "gloves")
        inHandItemModel(SOFItems.DIAMOND_GLOVES, "gloves")
        inHandItemModel(SOFItems.NETHERITE_GLOVES, "gloves")
    }

    private fun inHandItemModel(item: DeferredHolder<Item, out Item>, context: String) {
        val id = item.id
        withExistingParent(id.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${id.path}")
            .texture("particle", "item/${id.path}")
        withExistingParent(id.toString() + "_in_hand", modLoc("item/${context}_in_hand"))
            .texture("0", "item/${id.path}_in_hand")
            .texture("particle", "item/${id.path}")
    }

}