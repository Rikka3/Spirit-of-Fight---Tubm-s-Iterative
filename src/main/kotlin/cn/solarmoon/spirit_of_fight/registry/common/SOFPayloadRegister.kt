package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.spirit.FightSpiritClearPayload
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritPayload
import cn.solarmoon.spirit_of_fight.sync.MoveDirectionPayload
import cn.solarmoon.spirit_of_fight.sync.MovePayload
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler

object SOFPayloadRegister {

    private fun net(event: RegisterPayloadHandlersEvent) {
        val control = event.registrar("client_control")
        control.playBidirectional(MoveDirectionPayload.TYPE, MoveDirectionPayload.STREAM_CODEC, DirectionalPayloadHandler(MoveDirectionPayload::handle, MoveDirectionPayload::handle))
        control.playToClient(MovePayload.TYPE, MovePayload.STREAM_CODEC, MovePayload::handleInClient)
        control.playToServer(FightSpiritClearPayload.TYPE, FightSpiritClearPayload.STREAM_CODEC, FightSpiritClearPayload::handleInServer)
        val sync = event.registrar("sync")
        sync.playToClient(FightSpiritPayload.TYPE, FightSpiritPayload.STREAM_CODEC, FightSpiritPayload::handleInClient)
    }

    @JvmStatic
    fun register(modBus: IEventBus) {
        modBus.addListener(::net)
    }

}