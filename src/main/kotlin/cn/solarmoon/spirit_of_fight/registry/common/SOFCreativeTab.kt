package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

object SOFCreativeTab {
    @JvmStatic
    fun register(bus: IEventBus) { bus.addListener(::testCT) }

    @JvmStatic
    val MAIN = SpiritOfFight.REGISTER.creativeTab()
        .id(SpiritOfFight.MOD_ID)
        .bound(CreativeModeTab.builder()
            .title(Component.translatable("creative_mode_tab." + SpiritOfFight.MOD_ID + ".main"))
            .icon { ItemStack(SOFItems.IRON_SPEAR) }
            .displayItems { params, output ->
                output.accept(ItemStack(SOFItems.LEATHER_GLOVES.get()))
                output.accept(ItemStack(SOFItems.IRON_GLOVES.get()))
                output.accept(ItemStack(SOFItems.GOLDEN_GLOVES.get()))
                output.accept(ItemStack(SOFItems.DIAMOND_GLOVES.get()))
                output.accept(ItemStack(SOFItems.NETHERITE_GLOVES.get()))

                output.accept(ItemStack(Items.WOODEN_SWORD))
                output.accept(ItemStack(Items.STONE_SWORD))
                output.accept(ItemStack(Items.IRON_SWORD))
                output.accept(ItemStack(Items.GOLDEN_SWORD))
                output.accept(ItemStack(Items.DIAMOND_SWORD))
                output.accept(ItemStack(Items.NETHERITE_SWORD))

                output.accept(ItemStack(Items.WOODEN_AXE))
                output.accept(ItemStack(Items.STONE_AXE))
                output.accept(ItemStack(Items.IRON_AXE))
                output.accept(ItemStack(Items.GOLDEN_AXE))
                output.accept(ItemStack(Items.DIAMOND_AXE))
                output.accept(ItemStack(Items.NETHERITE_AXE))

                output.accept(ItemStack(SOFItems.WOODEN_SPEAR.get()))
                output.accept(ItemStack(SOFItems.STONE_SPEAR.get()))
                output.accept(ItemStack(SOFItems.IRON_SPEAR.get()))
                output.accept(ItemStack(SOFItems.GOLDEN_SPEAR.get()))
                output.accept(ItemStack(SOFItems.DIAMOND_SPEAR.get()))
                output.accept(ItemStack(SOFItems.NETHERITE_SPEAR.get()))

                output.accept(ItemStack(SOFItems.WOODEN_WARHAMMER.get()))
                output.accept(ItemStack(SOFItems.STONE_WARHAMMER.get()))
                output.accept(ItemStack(SOFItems.IRON_WARHAMMER.get()))
                output.accept(ItemStack(SOFItems.GOLDEN_WARHAMMER.get()))
                output.accept(ItemStack(SOFItems.DIAMOND_WARHAMMER.get()))
                output.accept(ItemStack(SOFItems.NETHERITE_WARHAMMER.get()))
            }
        )
        .build()

    private fun testCT(event: BuildCreativeModeTabContentsEvent) {
//        if (event.tabKey == CreativeModeTabs.COMBAT) {
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.LEATHER_GLOVES.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.IRON_GLOVES.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.GOLDEN_GLOVES.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.DIAMOND_GLOVES.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.NETHERITE_GLOVES.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.WOODEN_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.STONE_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.IRON_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.GOLDEN_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.DIAMOND_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.NETHERITE_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.WOODEN_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.STONE_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.IRON_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.GOLDEN_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.DIAMOND_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.NETHERITE_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
//        }
    }

}