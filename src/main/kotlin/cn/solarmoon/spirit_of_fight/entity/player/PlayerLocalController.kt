package cn.solarmoon.spirit_of_fight.entity.player

import cn.solarmoon.spark_core.animation.anim.play.layer.DefaultLayer
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.event.PlayerRenderAnimInFirstPersonEvent
import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.local_control.getPressTickTime
import cn.solarmoon.spark_core.local_control.onEvent
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.debug.SimpleBlockDebug
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.ui.SkillTreeSetScreen
import cn.solarmoon.spirit_of_fight.sync.MoveDirectionPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.HitResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.network.PacketDistributor

object PlayerLocalController {

    val attackKey get() = Minecraft.getInstance().options.keyAttack

    @SubscribeEvent
    private fun tick(event: MovementInputUpdateEvent) {
        val player = event.entity
        val input = event.input

        // Prevent skill activation while stunned or knocked down
        if (player.stunTicks > 0 || player.isKnockedDown) {
            val patch = (player as Any) as? IEntityPatch
            // Allow Switch Attack during Recovery Stun
            if (patch?.isRecoveryStun == true && SOFKeyMappings.SWITCH_POSTURE.get().isDown && !player.preInput.hasInput(SOFPreInputs.SWITCH_POSTURE)) {
                 // Check Switch Attack Cooldown
                 if (player.level().gameTime >= patch.switchAttackCooldownUntil) {
                     player.preInput.setInput(SOFPreInputs.SWITCH_POSTURE, 5) { }
                 }
            }

            input.up = false
            input.down = false
            input.left = false
            input.right = false
            input.jumping = false
            input.shiftKeyDown = false
            // Also clear any existing movement to prevent sliding
            input.forwardImpulse = 0f
            input.leftImpulse = 0f
            SimpleBlockDebug.log("Player stunned/knockedDown - movement blocked")
            
            // If we just buffered the switch attack, we might want to let tryAdvance check it?
            // But tryAdvance is called at the end of tick.
            // If we return here, tryAdvance won't run.
            // So IF recovery stun AND switch posture is allowed, we should probably NOT return?
            if (patch?.isRecoveryStun == true) {
                // proceed to tryAdvance, but inputs are cleared, so movement is blocked.
                // switch posture is in preInput now (or naturally in IEntityPatch/KeyMapping).
            } else {
                return
            }
        }

        SOFKeyMappings.OPEN_SKILL_TREE.get().onEvent(KeyEvent.PRESS_ONCE) {
            player.currentSkillSet?.let {
                SkillTreeSetScreen(it).open()
            }
            true
        }

        // 存储移动方向信息 (Only declare this ONCE)
        val direction = MoveDirection.getByInput((player as LocalPlayer).savedInput)
        if (player.moveDirection != direction) PacketDistributor.sendToServer(MoveDirectionPayload(player.id, direction?.ordinal ?: -1))
        player.moveDirection = direction

        // 移动加入预输入
        if (input.moveVector.length() > 0 && !player.preInput.hasInput && player.isPlayingSkill) {
            player.preInput.setInput(SOFPreInputs.MOVE, 5) {
                player.animController.getMainLayer().stopAnimation(10)
                player.animController.stopAnimToServer(DefaultLayer.MAIN_LAYER, 10)
            }
        }

        // 把跳跃加入预输入防止卡手
        if (player.isPlayingSkill && !player.isSwimming && !player.jumping && Minecraft.getInstance().options.keyJump.isDown && player.onGround()) {
            player.preInput.setInput(SOFPreInputs.JUMP, 5) {
                player.animController.getMainLayer().stopAnimation(10)
                player.animController.stopAnimToServer(DefaultLayer.MAIN_LAYER, 10)
                player.jumping = true
                player.jumpFromGround()
            }
        }

        // Handle BLOCK key input buffering
        if (SOFKeyMappings.BLOCK.get().isDown && !player.preInput.hasInput(SOFPreInputs.GUARD)) {
            player.preInput.setInput(SOFPreInputs.GUARD, 5) { }
        }

        // Handle SWITCH_POSTURE key input buffering
        if (SOFKeyMappings.SWITCH_POSTURE.get().isDown && !player.preInput.hasInput(SOFPreInputs.SWITCH_POSTURE) && !player.isPlayingSkill) {
            val patch = (player as Any) as? IEntityPatch
            // Check Cooldown
            if (patch != null && player.level().gameTime < patch.switchAttackCooldownUntil) {
                // Cooldown Active - Block Input
            } else {
                player.preInput.setInput(SOFPreInputs.SWITCH_POSTURE, 5) { }
            }
        }
        
        // Handle ATTACK key input buffering (Combo Cooldown)
        if (player.level().gameTime < ((player as Any) as? IEntityPatch)?.comboCooldownUntil ?: 0) {
            // Block Attack Input during Combo Cooldown
            input.up = false
            input.down = false
            input.left = false
            input.right = false
            input.jumping = false
            // input.shiftKeyDown = false // Allow sneaking?
            // Prevent normal attack registration
            // This might need to be done in onInteract or similar if we want to block the vanilla attack packet?
            // But preInput is for skills.
        }

        // Handle DODGE key input buffering
        if (SOFKeyMappings.DODGE.get().isDown && !player.preInput.hasInput(SOFPreInputs.DODGE) && !player.isPlayingSkill) {
            val patch = (player as Any) as? IEntityPatch
            val canDodge = player.isCreative || (patch != null && player.level().gameTime - patch.lastDodgeTick >= 20 && player.foodData.foodLevel >= 2)
            if (canDodge) {
                player.preInput.setInput(SOFPreInputs.DODGE, 5) { }
            }
        }

        if (player.currentSkillSet?.tryAdvance(player, input) == true) {
            if (!player.isPlayingSkill) {
                player.preInput.execute()
            }
        }
    }

    @SubscribeEvent
    fun onInteract(event: InputEvent.InteractionKeyMappingTriggered) {
        val player = Minecraft.getInstance().player ?: return

        if (player.stunTicks > 0 || player.isKnockedDown) {
            event.setSwingHand(false)
            event.isCanceled = true
            return
        }

        // 防止使用副手物品和格挡冲突
        if (event.isUseItem && event.hand == InteractionHand.OFF_HAND && player.currentSkillSet?.tryAdvance(player, player.input, true) == true && guardKeyConflict()) {
            event.setSwingHand(false)
            event.isCanceled = true
        }

        if (event.isAttack) {
            val patch = (player as Any) as? IEntityPatch
            if (patch != null && player.level().gameTime < patch.comboCooldownUntil) {
                event.setSwingHand(false)
                event.isCanceled = true
                return
            }
            
            if (shouldSkillAttack(player)) {
                event.setSwingHand(false)
                event.isCanceled = true
            }
        }
        else if (player.isPlayingSkill) {
            // 使用任何技能时都不能使用物品和交互
            event.setSwingHand(false)
            event.isCanceled = true
        }
    }

    fun shouldSkillAttack(player: LocalPlayer): Boolean {
        if (player.currentSkillSet == null || player.isSpectator) return false
        if (player.stunTicks > 0 || player.isKnockedDown) return false
        val hit = Minecraft.getInstance().hitResult ?: return false
        // 如果目标是空气或实体，则无论如何默认进行攻击
        return if (hit.type in listOf(HitResult.Type.ENTITY, HitResult.Type.MISS)) true
        // 如果是方块，则看是否按压时间短于0.15秒，超出则正常挖掘
        else attackKey.getPressTickTime() <= 3
    }

    fun guardKeyConflict() = SOFKeyMappings.BLOCK.get().isDown && SOFKeyMappings.BLOCK.get().key.value == Minecraft.getInstance().options.keyUse.key.value

    @SubscribeEvent
    private fun fpa(event: PlayerRenderAnimInFirstPersonEvent) {
        val player = event.player
        if (player.isPlayingSkill) {
            event.shouldRender = true
        }
    }

}