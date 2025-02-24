package cn.solarmoon.spirit_of_fight.skill.tree.ui

import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet
import net.minecraft.client.gui.Font
import org.joml.Vector2i
import kotlin.math.max

class SkillTreeSetLayout(
    private val treeSet: SkillTreeSet,
    private val font: Font,
    private val verticalDrop: Int
): LinkedHashMap<SkillTree, SkillTreeLayout>() {

    val internal = 4
    var totalWidth = 0
        private set
    var totalHeight = 0
        private set

    init {
        calculateLayout()
    }

    private fun calculateLayout() {
        var xPos = 0
        treeSet.forEachIndexed { index, tree ->
            val layout = SkillTreeLayout(tree, font, verticalDrop)
            layout.pos = Vector2i(xPos, 0)
            this[tree] = layout
            val next = treeSet.elementAtOrNull(index + 1)?.let { SkillTreeLayout(it, font, verticalDrop).totalWidth / 2 } ?: 0
            xPos += (layout.totalWidth / 2 + next + internal)
            totalHeight = max(totalHeight, layout.totalHeight)
        }
        totalWidth = xPos
    }

}