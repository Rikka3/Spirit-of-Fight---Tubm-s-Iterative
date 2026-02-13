package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.particle.sync.QuadraticParticlePayload
import cn.solarmoon.spirit_of_fight.poise_system.PoiseResetPayload
import cn.solarmoon.spirit_of_fight.poise_system.PoiseSetPayload
import cn.solarmoon.spirit_of_fight.skill.BlockBreakBarGui
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritClearPayload
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritPayload
import cn.solarmoon.spirit_of_fight.sync.BlockBreakProgressPayload
import cn.solarmoon.spirit_of_fight.sync.DodgeCooldownPayload
import cn.solarmoon.spirit_of_fight.sync.DodgeCostPayload
import cn.solarmoon.spirit_of_fight.sync.MoveDirectionPayload
import cn.solarmoon.spirit_of_fight.sync.SwitchAttackCooldownPayload
import cn.solarmoon.spirit_of_fight.sync.ComboCooldownPayload
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler

object SOFPayloadRegister {

    private fun net(event: RegisterPayloadHandlersEvent) {
        val control = event.registrar("client_control")
        control.playBidirectional(MoveDirectionPayload.TYPE, MoveDirectionPayload.STREAM_CODEC, DirectionalPayloadHandler(MoveDirectionPayload::handle, MoveDirectionPayload::handle))
        control.playToServer(FightSpiritClearPayload.TYPE, FightSpiritClearPayload.STREAM_CODEC, FightSpiritClearPayload::handleInServer)
        control.playToServer(DodgeCostPayload.TYPE, DodgeCostPayload.STREAM_CODEC, DodgeCostPayload::handleInServer)
        
        val sync = event.registrar("sync")
        sync.playToClient(FightSpiritPayload.TYPE, FightSpiritPayload.STREAM_CODEC, FightSpiritPayload::handleInClient)
        sync.playToClient(PoiseSetPayload.TYPE, PoiseSetPayload.STREAM_CODEC, PoiseSetPayload::handleInClient)
        sync.playToClient(PoiseResetPayload.TYPE, PoiseResetPayload.STREAM_CODEC, PoiseResetPayload::handleInClient)
        sync.playToClient(DodgeCooldownPayload.TYPE, DodgeCooldownPayload.STREAM_CODEC, DodgeCooldownPayload::handleInClient)
        sync.playToClient(SwitchAttackCooldownPayload.TYPE, SwitchAttackCooldownPayload.STREAM_CODEC, SwitchAttackCooldownPayload::handleInClient)
        sync.playToClient(ComboCooldownPayload.TYPE, ComboCooldownPayload.STREAM_CODEC, ComboCooldownPayload::handleInClient)
        sync.playToClient(cn.solarmoon.spirit_of_fight.sync.RecoveryStunPayload.TYPE, cn.solarmoon.spirit_of_fight.sync.RecoveryStunPayload.STREAM_CODEC, cn.solarmoon.spirit_of_fight.sync.RecoveryStunPayload::handleInClient)
        sync.playToClient(BlockBreakProgressPayload.TYPE, BlockBreakProgressPayload.STREAM_CODEC, BlockBreakProgressPayload::handleInClient)
        val particle = event.registrar("particle")
        particle.playToClient(QuadraticParticlePayload.TYPE, QuadraticParticlePayload.STREAM_CODEC, QuadraticParticlePayload::handleInClient)
    }

    @JvmStatic
    fun register(modBus: IEventBus) {
        modBus.addListener(::net)
    }

}