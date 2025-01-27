package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.preset_anim.CommonState
import cn.solarmoon.spark_core.animation.preset_anim.EntityStates
import cn.solarmoon.spark_core.entity.moveCheck
import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spark_core.registry.common.SparkTypedAnimations
import cn.solarmoon.spark_core.skill.controller.getSkillController
import cn.solarmoon.spark_core.skill.controller.getTypedSkillController
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
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

            if (player.getSkillController() is BaimeiFightSkillController) {
                when(event.state) {
                    EntityStates.Idle -> event.newAnim = SOFTypedAnimations.BAIMEI_IDLE.get()
                    EntityStates.Walk -> event.newAnim = SOFTypedAnimations.BAIMEI_WALK.get()
                    EntityStates.WalkBack -> event.newAnim = SOFTypedAnimations.BAIMEI_WALK_BACK.get()
                    EntityStates.Sprinting -> event.newAnim = SOFTypedAnimations.BAIMEI_SPRINTING.get()
                }
            }
        }
    }

    @SubscribeEvent
    private fun transModifier(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        if (player.isLocalPlayer && player is LocalPlayer) {
            if (event.state == EntityStates.Idle && PlayerLocalController.moveRemain > 0) {
                event.isCanceled = true
            }

            if (PlayerLocalController.moveRemain > 0) {
                event.transitionTime = 7
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