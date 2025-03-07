package cn.solarmoon.spirit_of_fight.registry.common

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

object SOFCreativeTab {
    @JvmStatic
    fun register(bus: IEventBus) { bus.addListener(::testCT) }

//    @JvmStatic
//    val MAIN = SpiritOfFight.REGISTER.creativeTab()
//        .id(SpiritOfFight.MOD_ID)
//        .bound(CreativeModeTab.builder()
//            .title(Component.translatable("creative_mode_tab" + SpiritOfFight.MOD_ID + "main"))
//            .icon { ItemStack(SOFItems.IRON_HAMMER) }
//            .displayItems { params, output ->
//                SpiritOfFight.REGISTER.itemDeferredRegister.entries.forEach {
//                    output.accept(it.value())
//                }
//            }
//        )
//        .build()

    private fun testCT(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.COMBAT) {
            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.WOODEN_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.STONE_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.IRON_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.GOLDEN_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.DIAMOND_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.NETHERITE_WARHAMMER.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)

            event.insertBefore(ItemStack(Items.TRIDENT), ItemStack(SOFItems.GLOVES.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
        }
    }

}