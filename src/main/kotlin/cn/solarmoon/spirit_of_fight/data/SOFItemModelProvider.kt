package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class SOFItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
): ItemModelProvider(output, SpiritOfFight.MOD_ID, existingFileHelper) {

    override fun registerModels() {
        val woodenWarHammer = SOFItems.WOODEN_WARHAMMER.id
        withExistingParent(woodenWarHammer.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${woodenWarHammer.path}")
            .texture("particle", "item/${woodenWarHammer.path}")
        withExistingParent(woodenWarHammer.toString() + "_in_hand", modLoc("item/warhammer_in_hand"))
            .texture("0", "item/${woodenWarHammer.path}_in_hand")
            .texture("particle", "item/${woodenWarHammer.path}")

        val stoneWarHammer = SOFItems.STONE_WARHAMMER.id
        withExistingParent(stoneWarHammer.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${stoneWarHammer.path}")
            .texture("particle", "item/${stoneWarHammer.path}")
        withExistingParent(stoneWarHammer.toString() + "_in_hand", modLoc("item/warhammer_in_hand"))
            .texture("0", "item/${stoneWarHammer.path}_in_hand")
            .texture("particle", "item/${stoneWarHammer.path}")

        val ironWarHammer = SOFItems.IRON_WARHAMMER.id
        withExistingParent(ironWarHammer.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${ironWarHammer.path}")
            .texture("particle", "item/${ironWarHammer.path}")
        withExistingParent(ironWarHammer.toString() + "_in_hand", modLoc("item/warhammer_in_hand"))
            .texture("0", "item/${ironWarHammer.path}_in_hand")
            .texture("particle", "item/${ironWarHammer.path}")

        val goldenWarHammer = SOFItems.GOLDEN_WARHAMMER.id
        withExistingParent(goldenWarHammer.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${goldenWarHammer.path}")
            .texture("particle", "item/${goldenWarHammer.path}")
        withExistingParent(goldenWarHammer.toString() + "_in_hand", modLoc("item/warhammer_in_hand"))
            .texture("0", "item/${goldenWarHammer.path}_in_hand")
            .texture("particle", "item/${goldenWarHammer.path}")

        val diamondWarHammer = SOFItems.DIAMOND_WARHAMMER.id
        withExistingParent(diamondWarHammer.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${diamondWarHammer.path}")
            .texture("particle", "item/${diamondWarHammer.path}")
        withExistingParent(diamondWarHammer.toString() + "_in_hand", modLoc("item/warhammer_in_hand"))
            .texture("0", "item/${diamondWarHammer.path}_in_hand")
            .texture("particle", "item/${diamondWarHammer.path}")

        val netheriteWarHammer = SOFItems.NETHERITE_WARHAMMER.id
        withExistingParent(netheriteWarHammer.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${netheriteWarHammer.path}")
            .texture("particle", "item/${netheriteWarHammer.path}")
        withExistingParent(netheriteWarHammer.toString() + "_in_hand", modLoc("item/warhammer_in_hand"))
            .texture("0", "item/${netheriteWarHammer.path}_in_hand")
            .texture("particle", "item/${netheriteWarHammer.path}")

        val gloves = SOFItems.GLOVES.id
        withExistingParent(gloves.toString(), mcLoc("item/generated"))
            .texture("layer0", "item/${gloves.path}")
            .texture("particle", "item/${gloves.path}")
    }

}