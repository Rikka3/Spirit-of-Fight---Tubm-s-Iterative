package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.hit.AttackModifier
import cn.solarmoon.spirit_of_fight.skill.component.SkillComponentApplier
import cn.solarmoon.spirit_of_fight.spirit.FightSpiritApplier
import cn.solarmoon.spirit_of_fight.hit.HitAnimationApplier
import cn.solarmoon.spirit_of_fight.fighter.PatchApplier
import cn.solarmoon.spirit_of_fight.skill.controller.FightStateAnimModifier
import net.neoforged.neoforge.common.NeoForge

object SOFCommonEventRegister {

    @JvmStatic
    fun register() {
        add(AttackModifier)
        add(FightSpiritApplier)
        add(HitAnimationApplier)
        add(PatchApplier)
        add(SkillComponentApplier)
        add(FightStateAnimModifier)
    }

    private fun add(event: Any) {
        NeoForge.EVENT_BUS.register(event)
    }

}