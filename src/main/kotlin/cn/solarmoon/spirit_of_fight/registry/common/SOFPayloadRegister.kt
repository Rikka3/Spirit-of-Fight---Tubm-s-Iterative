package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritPayload
import cn.solarmoon.spirit_of_fight.sync.MovePayload
import cn.solarmoon.spirit_of_fight.hit.HitAnimPayload
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler

object SOFPayloadRegister {

    private fun net(event: RegisterPayloadHandlersEvent) {
        val control = event.registrar("client_control")
        control.playBidirectional(ClientOperationPayload.TYPE, ClientOperationPayload.STREAM_CODEC, DirectionalPayloadHandler(ClientOperationPayload::handle, ClientOperationPayload::handle))
        control.playToClient(MovePayload.TYPE, MovePayload.STREAM_CODEC, MovePayload::handleInClient)
        val sync = event.registrar("sync")
        sync.playToClient(FightSpiritPayload.TYPE, FightSpiritPayload.STREAM_CODEC, FightSpiritPayload::handleInClient)
        sync.playToClient(HitAnimPayload.TYPE, HitAnimPayload.STREAM_CODEC, HitAnimPayload::handleInClient)
    }

    @JvmStatic
    fun register(modBus: IEventBus) {
        modBus.addListener(::net)
    }

}