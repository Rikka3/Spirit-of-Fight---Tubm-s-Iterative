package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.SkillComponent
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class AnimAddFightSpiritComponent(
    val attacker: Entity
): SkillComponent {

    fun onHurt(event: LivingIncomingDamageEvent) {

    }

}