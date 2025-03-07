package cn.solarmoon.spirit_of_fight.registry.client

import cn.solarmoon.spirit_of_fight.registry.common.SOFItems
import net.minecraft.client.resources.model.ModelResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.ModelEvent

object SOFItemBakedModelModifier {

    private fun modifyBakingResult(event: ModelEvent.ModifyBakingResult) {

    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::modifyBakingResult)
    }

}