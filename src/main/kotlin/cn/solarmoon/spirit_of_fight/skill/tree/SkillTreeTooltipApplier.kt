package cn.solarmoon.spirit_of_fight.skill.tree

import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

object SkillTreeTooltipApplier {

    @SubscribeEvent
    private fun tooltip(event: ItemTooltipEvent) {
        val item = event.itemStack
        val player = event.entity ?: return

    }

}