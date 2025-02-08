package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.fighter.player.PlayerBodyApplier
import cn.solarmoon.spirit_of_fight.skill.controller.SkillGroupControllerApplier
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritApplier
import net.neoforged.neoforge.common.NeoForge

object SOFCommonEventRegister {

    @JvmStatic
    fun register() {
        add(FightSpiritApplier)
        add(SkillGroupControllerApplier)
        add(PlayerBodyApplier)
    }

    private fun add(event: Any) {
        NeoForge.EVENT_BUS.register(event)
    }

}