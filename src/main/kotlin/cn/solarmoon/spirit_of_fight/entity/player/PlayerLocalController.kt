package cn.solarmoon.spirit_of_fight.entity.player

import cn.solarmoon.spark_core.event.PlayerRenderAnimInFirstPersonEvent
import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.local_control.getPressTickTime
import cn.solarmoon.spark_core.local_control.onEvent
import cn.solarmoon.spark_core.util.MoveDirection
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

        SOFKeyMappings.OPEN_SKILL_TREE.onEvent(KeyEvent.PRESS_ONCE) {
            player.currentSkillSet?.let {
                Minecraft.getInstance().setScreen(SkillTreeSetScreen(it))
            }
            true
        }

        // 存储移动方向信息
        val direction = MoveDirection.getByInput((player as LocalPlayer).savedInput)
        if (player.moveDirection != direction) PacketDistributor.sendToServer(MoveDirectionPayload(player.id, direction?.ordinal ?: -1))
        player.moveDirection = direction

        // 移动加入预输入
        if (input.moveVector.length() > 0 && !player.preInput.hasInput && player.isPlayingSkill) {
            player.preInput.setInput(SOFPreInputs.MOVE, 1, -1) {
                player.animController.stopAnimation()
            }
        }

        // 把跳跃加入预输入防止卡手
        if (player.isPlayingSkill && !player.isSwimming && !player.jumping && Minecraft.getInstance().options.keyJump.isDown && player.onGround()) {
            player.preInput.setInput(SOFPreInputs.JUMP, 5) {
                player.jumping = true
                player.jumpFromGround()
            }
        }

        player.currentSkillSet?.tryAdvance(player, input)
    }

    @SubscribeEvent
    fun onInteract(event: InputEvent.InteractionKeyMappingTriggered) {
        val player = Minecraft.getInstance().player ?: return

        // 防止使用副手物品和格挡冲突
        if (event.isUseItem && event.hand == InteractionHand.OFF_HAND && player.currentSkillSet?.tryAdvance(player, player.input, true) == true && guardKeyConflict()) {
            event.setSwingHand(false)
            event.isCanceled = true
        }

        if (event.isAttack && shouldSkillAttack(player)) {
            event.setSwingHand(false)
            event.isCanceled = true
        }
        else if (player.isPlayingSkill) {
            // 使用任何技能时都不能使用物品和交互
            event.setSwingHand(false)
            event.isCanceled = true
        }
    }

    fun shouldSkillAttack(player: LocalPlayer): Boolean {
        if (player.currentSkillSet == null || player.isSpectator) return false
        val hit = Minecraft.getInstance().hitResult ?: return false
        // 如果目标是空气或实体，则无论如何默认进行攻击
        return if (hit.type in listOf(HitResult.Type.ENTITY, HitResult.Type.MISS)) true
        // 如果是方块，则看是否按压时间短于0.15秒，超出则正常挖掘
        else attackKey.getPressTickTime() <= 3
    }

    fun guardKeyConflict() = SOFKeyMappings.GUARD.isDown && SOFKeyMappings.GUARD.key.value == Minecraft.getInstance().options.keyUse.key.value

    @SubscribeEvent
    private fun fpa(event: PlayerRenderAnimInFirstPersonEvent) {
        val player = event.player
        if (player.isPlayingSkill) {
            event.shouldRender = true
        }
    }

}