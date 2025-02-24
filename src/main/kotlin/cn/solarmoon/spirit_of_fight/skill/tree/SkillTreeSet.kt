package cn.solarmoon.spirit_of_fight.skill.tree

import net.minecraft.util.SortedArraySet
import java.util.TreeSet

class SkillTreeSet: LinkedHashSet<SkillTree> {
    constructor() : super()
    constructor(elements: Collection<SkillTree>) : super(elements)

    fun get() = SkillTreeSet(sortedByDescending { it.priority })

}