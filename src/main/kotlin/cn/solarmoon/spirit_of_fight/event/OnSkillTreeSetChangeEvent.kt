package cn.solarmoon.spirit_of_fight.event

import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.Event

class OnSkillTreeSetChangeEvent(
    val player: Player,
    val oldSet: SkillTreeSet?,
    val originNewSet: SkillTreeSet?
): Event() {

    var newSet = originNewSet

}