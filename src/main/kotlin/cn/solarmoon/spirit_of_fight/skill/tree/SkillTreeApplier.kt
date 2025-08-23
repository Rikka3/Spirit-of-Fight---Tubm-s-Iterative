package cn.solarmoon.spirit_of_fight.skill.tree

import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.event.OnSkillTreeSetChangeEvent
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.tick.EntityTickEvent

object SkillTreeApplier {

    @SubscribeEvent
    private fun tick(event: EntityTickEvent.Pre) {
        val entity = event.entity
        if (entity !is Player) return

        if (entity.currentSkillSet != entity.getSkillTrees()) {
            entity.onSkillSetChanged(entity.currentSkillSet, entity.getSkillTrees())
        }
    }

    private fun Player.onSkillSetChanged(last: SkillTreeSet?, new: SkillTreeSet?) {
        val event = NeoForge.EVENT_BUS.post(OnSkillTreeSetChangeEvent(this, last, new))
        if (true && this.isLocalPlayer) {
            last?.forEach {
                it.currentSkill?.apply {
                    endOnClient()
                }
                it.reset(this)
            }
        }
        wieldStyle = WieldStyle.DEFAULT
        currentSkillSet = event.newSet
    }

}