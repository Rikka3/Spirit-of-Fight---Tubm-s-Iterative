package cn.solarmoon.spirit_of_fight.registry.client

import cn.solarmoon.spirit_of_fight.entity.player.CameraAdjuster
import cn.solarmoon.spirit_of_fight.entity.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.lock_on.LockOnApplier
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritGui
import net.neoforged.neoforge.common.NeoForge

object SOFClientEventRegister {

    @JvmStatic
    fun register() {
        add(CameraAdjuster)
        add(LockOnApplier)
        add(PlayerLocalController)
        add(FightSpiritGui)
    }

    private fun add(event: Any) {
        NeoForge.EVENT_BUS.register(event)
    }

}