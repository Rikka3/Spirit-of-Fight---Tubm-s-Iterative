package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.concurrent.CompletableFuture

class SOFRecipeProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, lookupProvider) {

    override fun buildRecipes(writer: RecipeOutput) {
        // Gloves recipes
        buildGlovesRecipes(writer)
        
        // Warhammers recipes
        buildWarhammerRecipes(writer)
        
        // Spears recipes
        buildSpearRecipes(writer)
        
        // Battleaxes recipes
        buildBattleaxeRecipes(writer)
        
        // Greatswords recipes
        buildGreatswordRecipes(writer)
    }

    // ==================== GLOVES RECIPES ====================
    private fun buildGlovesRecipes(writer: RecipeOutput) {
        // Leather Gloves - 2 leather in vertical line
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.LEATHER_GLOVES.get())
            .pattern(" M ")
            .pattern(" M ")
            .define('M', Items.LEATHER)
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(writer, id("leather_gloves"))

        // Iron Gloves - 2 iron ingots + 1 leather (vertical line with leather at bottom)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.IRON_GLOVES.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" L ")
            .define('M', Items.IRON_INGOT)
            .define('L', Items.LEATHER)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(writer, id("iron_gloves"))

        // Golden Gloves - 2 gold ingots + 1 leather
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.GOLDEN_GLOVES.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" L ")
            .define('M', Items.GOLD_INGOT)
            .define('L', Items.LEATHER)
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(writer, id("golden_gloves"))

        // Diamond Gloves - 2 diamonds + 1 leather
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.DIAMOND_GLOVES.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" L ")
            .define('M', Items.DIAMOND)
            .define('L', Items.LEATHER)
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(writer, id("diamond_gloves"))

        // Netherite Gloves - Smithing upgrade
        netheriteSmithing(writer, SOFItems.DIAMOND_GLOVES, SOFItems.NETHERITE_GLOVES, "netherite_gloves")
    }

    // ==================== WARHAMMER RECIPES ====================
    private fun buildWarhammerRecipes(writer: RecipeOutput) {
        // Wooden Warhammer - 5 planks + 2 sticks (T-shape)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.WOODEN_WARHAMMER.get())
            .pattern("MMM")
            .pattern(" M ")
            .pattern(" S ")
            .define('M', ItemTags.PLANKS)
            .define('S', Items.STICK)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(writer, id("wooden_warhammer"))

        // Stone Warhammer - 5 cobblestone + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.STONE_WARHAMMER.get())
            .pattern("MMM")
            .pattern(" M ")
            .pattern(" S ")
            .define('M', ItemTags.STONE_CRAFTING_MATERIALS)
            .define('S', Items.STICK)
            .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
            .save(writer, id("stone_warhammer"))

        // Iron Warhammer - 5 iron ingots + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.IRON_WARHAMMER.get())
            .pattern("MMM")
            .pattern(" M ")
            .pattern(" S ")
            .define('M', Items.IRON_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(writer, id("iron_warhammer"))

        // Golden Warhammer - 5 gold ingots + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.GOLDEN_WARHAMMER.get())
            .pattern("MMM")
            .pattern(" M ")
            .pattern(" S ")
            .define('M', Items.GOLD_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(writer, id("golden_warhammer"))

        // Diamond Warhammer - 5 diamonds + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.DIAMOND_WARHAMMER.get())
            .pattern("MMM")
            .pattern(" M ")
            .pattern(" S ")
            .define('M', Items.DIAMOND)
            .define('S', Items.STICK)
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(writer, id("diamond_warhammer"))

        // Netherite Warhammer - Smithing upgrade
        netheriteSmithing(writer, SOFItems.DIAMOND_WARHAMMER, SOFItems.NETHERITE_WARHAMMER, "netherite_warhammer")
    }

    // ==================== SPEAR RECIPES ====================
    private fun buildSpearRecipes(writer: RecipeOutput) {
        // Wooden Spear - 1 plank + 2 sticks (vertical line)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.WOODEN_SPEAR.get())
            .pattern(" M ")
            .pattern(" S ")
            .pattern(" S ")
            .define('M', ItemTags.PLANKS)
            .define('S', Items.STICK)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(writer, id("wooden_spear"))

        // Stone Spear - 1 cobblestone + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.STONE_SPEAR.get())
            .pattern(" M ")
            .pattern(" S ")
            .pattern(" S ")
            .define('M', ItemTags.STONE_CRAFTING_MATERIALS)
            .define('S', Items.STICK)
            .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
            .save(writer, id("stone_spear"))

        // Iron Spear - 1 iron ingot + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.IRON_SPEAR.get())
            .pattern(" M ")
            .pattern(" S ")
            .pattern(" S ")
            .define('M', Items.IRON_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(writer, id("iron_spear"))

        // Golden Spear - 1 gold ingot + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.GOLDEN_SPEAR.get())
            .pattern(" M ")
            .pattern(" S ")
            .pattern(" S ")
            .define('M', Items.GOLD_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(writer, id("golden_spear"))

        // Diamond Spear - 1 diamond + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.DIAMOND_SPEAR.get())
            .pattern(" M ")
            .pattern(" S ")
            .pattern(" S ")
            .define('M', Items.DIAMOND)
            .define('S', Items.STICK)
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(writer, id("diamond_spear"))

        // Netherite Spear - Smithing upgrade
        netheriteSmithing(writer, SOFItems.DIAMOND_SPEAR, SOFItems.NETHERITE_SPEAR, "netherite_spear")
    }

    // ==================== BATTLEAXE RECIPES ====================
    private fun buildBattleaxeRecipes(writer: RecipeOutput) {
        // Wooden Battleaxe - 5 planks + 2 sticks (L-shape)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.WOODEN_BATTLEAXE.get())
            .pattern("MM ")
            .pattern("MMS")
            .pattern(" S ")
            .define('M', ItemTags.PLANKS)
            .define('S', Items.STICK)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(writer, id("wooden_battleaxe"))

        // Stone Battleaxe - 5 cobblestone + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.STONE_BATTLEAXE.get())
            .pattern("MM ")
            .pattern("MMS")
            .pattern(" S ")
            .define('M', ItemTags.STONE_CRAFTING_MATERIALS)
            .define('S', Items.STICK)
            .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
            .save(writer, id("stone_battleaxe"))

        // Iron Battleaxe - 5 iron ingots + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.IRON_BATTLEAXE.get())
            .pattern("MM ")
            .pattern("MMS")
            .pattern(" S ")
            .define('M', Items.IRON_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(writer, id("iron_battleaxe"))

        // Golden Battleaxe - 5 gold ingots + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.GOLDEN_BATTLEAXE.get())
            .pattern("MM ")
            .pattern("MMS")
            .pattern(" S ")
            .define('M', Items.GOLD_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(writer, id("golden_battleaxe"))

        // Diamond Battleaxe - 5 diamonds + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.DIAMOND_BATTLEAXE.get())
            .pattern("MM ")
            .pattern("MMS")
            .pattern(" S ")
            .define('M', Items.DIAMOND)
            .define('S', Items.STICK)
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(writer, id("diamond_battleaxe"))

        // Netherite Battleaxe - Smithing upgrade
        netheriteSmithing(writer, SOFItems.DIAMOND_BATTLEAXE, SOFItems.NETHERITE_BATTLEAXE, "netherite_battleaxe")
    }

    // ==================== GREATSWORD RECIPES ====================
    private fun buildGreatswordRecipes(writer: RecipeOutput) {
        // Wooden Greatsword - 3 planks + 2 sticks (vertical blade with cross guard)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.WOODEN_GREATSWORD.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" M ")
            .pattern("S S")
            .define('M', ItemTags.PLANKS)
            .define('S', Items.STICK)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(writer, id("wooden_greatsword"))

        // Stone Greatsword - 3 cobblestone + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.STONE_GREATSWORD.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" M ")
            .pattern("S S")
            .define('M', ItemTags.STONE_CRAFTING_MATERIALS)
            .define('S', Items.STICK)
            .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
            .save(writer, id("stone_greatsword"))

        // Iron Greatsword - 3 iron ingots + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.IRON_GREATSWORD.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" M ")
            .pattern("S S")
            .define('M', Items.IRON_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(writer, id("iron_greatsword"))

        // Golden Greatsword - 3 gold ingots + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.GOLDEN_GREATSWORD.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" M ")
            .pattern("S S")
            .define('M', Items.GOLD_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(writer, id("golden_greatsword"))

        // Diamond Greatsword - 3 diamonds + 2 sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SOFItems.DIAMOND_GREATSWORD.get())
            .pattern(" M ")
            .pattern(" M ")
            .pattern(" M ")
            .pattern("S S")
            .define('M', Items.DIAMOND)
            .define('S', Items.STICK)
            .unlockedBy("has_diamond", has(Items.DIAMOND))
            .save(writer, id("diamond_greatsword"))

        // Netherite Greatsword - Smithing upgrade
        netheriteSmithing(writer, SOFItems.DIAMOND_GREATSWORD, SOFItems.NETHERITE_GREATSWORD, "netherite_greatsword")
    }

    // ==================== HELPER METHODS ====================
    
    private fun netheriteSmithing(
        writer: RecipeOutput,
        baseItem: DeferredHolder<Item, out Item>,
        resultItem: DeferredHolder<Item, out Item>,
        recipeName: String
    ) {
        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(baseItem.get()),
            Ingredient.of(Items.NETHERITE_INGOT),
            RecipeCategory.COMBAT,
            resultItem.get()
        ).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
            .save(writer, id(recipeName))
    }

    private fun id(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, path)
    }
}
