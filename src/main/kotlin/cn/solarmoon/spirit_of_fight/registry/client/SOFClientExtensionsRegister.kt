package cn.solarmoon.spirit_of_fight.registry.client

import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.IModBusEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

object SOFClientExtensionsRegister {

    private fun reg(event: RegisterClientExtensionsEvent) {

    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}