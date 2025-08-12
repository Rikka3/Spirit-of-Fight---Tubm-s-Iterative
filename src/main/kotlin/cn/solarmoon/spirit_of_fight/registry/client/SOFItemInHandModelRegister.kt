package cn.solarmoon.spirit_of_fight.registry.client

import cn.solarmoon.spark_core.event.ItemInHandModelRegisterEvent
import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.neoforged.bus.api.IEventBus

object SOFItemInHandModelRegister {

    private fun reg(event: ItemInHandModelRegisterEvent) {
        event.addInHandModel(SOFItems.WOODEN_WARHAMMER.get())
        event.addInHandModel(SOFItems.STONE_WARHAMMER.get())
        event.addInHandModel(SOFItems.IRON_WARHAMMER.get())
        event.addInHandModel(SOFItems.GOLDEN_WARHAMMER.get())
        event.addInHandModel(SOFItems.DIAMOND_WARHAMMER.get())
        event.addInHandModel(SOFItems.NETHERITE_WARHAMMER.get())

        event.addInHandModel(SOFItems.WOODEN_SPEAR.get())
        event.addInHandModel(SOFItems.STONE_SPEAR.get())
        event.addInHandModel(SOFItems.IRON_SPEAR.get())
        event.addInHandModel(SOFItems.GOLDEN_SPEAR.get())
        event.addInHandModel(SOFItems.DIAMOND_SPEAR.get())
        event.addInHandModel(SOFItems.NETHERITE_SPEAR.get())

        event.addInHandModel(SOFItems.LEATHER_GLOVES.get())
        event.addInHandModel(SOFItems.IRON_GLOVES.get())
        event.addInHandModel(SOFItems.GOLDEN_GLOVES.get())
        event.addInHandModel(SOFItems.DIAMOND_GLOVES.get())
        event.addInHandModel(SOFItems.NETHERITE_GLOVES.get())
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}