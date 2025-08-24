package cn.solarmoon.spirit_of_fight.skill.tree

import net.minecraft.client.player.Input
import net.minecraft.world.entity.player.Player

class SkillTreeSet: LinkedHashSet<SkillTree> {
    constructor() : super()
    constructor(elements: Collection<SkillTree>) : super(elements)

    fun tryAdvance(player: Player, input: Input, simulate: Boolean = false): Boolean {
        if (player.isSpectator) return false

        var advanced = false
        forEach { if (it.tryAdvance(player, input, simulate)) advanced = true }
        return advanced
    }


    fun sortedByPriority() = SkillTreeSet(sortedByDescending { it.priority })

}