package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.event.MolangQueryRegisterEvent
import net.neoforged.bus.api.IEventBus

object SOFMolangQueryRegister {

    private fun reg(event: MolangQueryRegisterEvent) {
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}