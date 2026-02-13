package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.entity.StateAnimApplier
import cn.solarmoon.spirit_of_fight.entity.grab.GrabApplier
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.skill.BlockDamageHandler
import cn.solarmoon.spirit_of_fight.skill.StunHandler
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeApplier
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritApplier
import net.neoforged.neoforge.common.NeoForge

object SOFCommonEventRegister {

    @JvmStatic
    fun register() {
        add(FightSpiritApplier)
        add(StateAnimApplier)
        add(EntityHitApplier)
        add(SkillTreeApplier)
        add(GrabApplier)
        add(BlockDamageHandler)
        add(StunHandler)
        add(SOFSimpleDebugRegister)
    }

    private fun add(event: Any) {
        NeoForge.EVENT_BUS.register(event)
    }

}