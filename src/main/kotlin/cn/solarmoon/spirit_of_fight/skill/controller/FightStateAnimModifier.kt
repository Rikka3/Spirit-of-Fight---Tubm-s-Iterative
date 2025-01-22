package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.preset_anim.CommonState
import cn.solarmoon.spark_core.animation.preset_anim.EntityStates
import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spark_core.skill.controller.getSkillController
import cn.solarmoon.spirit_of_fight.registry.common.SOFTypedAnimations
import net.minecraft.client.player.LocalPlayer
import net.neoforged.bus.api.SubscribeEvent

object FightStateAnimModifier {

    @SubscribeEvent
    private fun playerState(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        if (player.isLocalPlayer && player is LocalPlayer) {
            if (player.getSkillController() is HammerFightSkillController) {
                when(event.state) {
                    EntityStates.Idle -> event.newAnim = SOFTypedAnimations.HAMMER_IDLE.get()
                    EntityStates.Walk -> event.newAnim = SOFTypedAnimations.HAMMER_WALK.get()
                    EntityStates.WalkBack -> event.newAnim = SOFTypedAnimations.HAMMER_WALK_BACK.get()
                    EntityStates.Sprinting -> event.newAnim = SOFTypedAnimations.HAMMER_SPRINTING.get()
                    EntityStates.Fall -> event.newAnim = SOFTypedAnimations.HAMMER_FALL.get()
                }
            }
        }
    }

    @SubscribeEvent
    private fun common(event: ChangePresetAnimEvent.Common) {
        val entity = event.entity
        if (entity.getSkillController() is HammerFightSkillController) {
            when(event.commonState) {
                CommonState.JUMP -> event.newAnim = "Common/hammer_jump_start"
            }
        }
    }

}