package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.animation.presets.EntityStates
import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.registry.common.SOFTypedAnimations
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.Tags

object StateAnimApplier {

    @SubscribeEvent
    private fun transModifier(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        if (player.mainHandItem.`is`(SOFItemTags.FORGE_HAMMER)) {
            when(event.state) {
                EntityStates.Idle -> event.newAnim = SOFTypedAnimations.HAMMER_IDLE.get()
                EntityStates.Walk -> event.newAnim = SOFTypedAnimations.HAMMER_WALK.get()
                EntityStates.WalkBack -> event.newAnim = SOFTypedAnimations.HAMMER_WALK_BACK.get()
                EntityStates.Sprinting -> event.newAnim = SOFTypedAnimations.HAMMER_SPRINTING.get()
                EntityStates.Fall -> event.newAnim = SOFTypedAnimations.HAMMER_FALL.get()
            }
        } else if (player.mainHandItem.`is`(SOFItemTags.FORGE_GLOVES)) {
            when(event.state) {
                EntityStates.Idle -> event.newAnim = SOFTypedAnimations.BAIMEI_IDLE.get()
                EntityStates.Walk -> event.newAnim = SOFTypedAnimations.BAIMEI_WALK.get()
                EntityStates.WalkBack -> event.newAnim = SOFTypedAnimations.BAIMEI_WALK_BACK.get()
                EntityStates.Sprinting -> event.newAnim = SOFTypedAnimations.BAIMEI_SPRINTING.get()
            }
        } else if (player.offhandItem.`is`(Tags.Items.TOOLS_SHIELD)) {
            when(event.state) {
                EntityStates.Sprinting -> event.newAnim = SOFTypedAnimations.SHIELD_SPRINTING.get()
            }
        }
    }

}