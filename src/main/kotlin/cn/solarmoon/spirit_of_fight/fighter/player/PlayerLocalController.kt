package cn.solarmoon.spirit_of_fight.fighter.player

import cn.solarmoon.spark_core.animation.vanilla.asAnimatable
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.local_control.getPressTickTime
import cn.solarmoon.spark_core.local_control.onEvent
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFCapabilities
import cn.solarmoon.spirit_of_fight.skill.tree.getSkillTrees
import cn.solarmoon.spirit_of_fight.skill.tree.ui.SkillTreeScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.phys.HitResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent

object PlayerLocalController {

    val attackKey get() = Minecraft.getInstance().options.keyAttack

    var moveRemain = 0

    @SubscribeEvent
    private fun tick(event: MovementInputUpdateEvent) {
        val player = event.entity
        val input = event.input

        player.getSkillTrees()?.firstOrNull { tree ->
            tree.tryAdvance(player, input)
        }

        SOFKeyMappings.SWITCH_ATTACK.onEvent(KeyEvent.PRESS_ONCE) {
            player.getSkillTrees()?.let {
                Minecraft.getInstance().setScreen(SkillTreeScreen(player.mainHandItem, it))
            }
            true
        }


        // 移动加入预输入
        if (input.moveVector.length() > 0 && !player.getPreInput().hasInput() && player.isPlayingSkill) {
            player.getPreInput().setInput("move", 1) {
                moveRemain = 5
                player.asAnimatable().animController.stopAnimation()
            }
        }
        if (moveRemain > 0) moveRemain--

        // 把跳跃加入预输入防止卡手
        if (player.isPlayingSkill && !player.mayFly() && !player.isSwimming && !player.jumping && Minecraft.getInstance().options.keyJump.isDown && player.onGround()) {
            player.getPreInput().setInput("jump", 5) {
                player.jumping = true
                player.jumpFromGround()
            }
        }
    }

    @SubscribeEvent
    fun onInteract(event: InputEvent.InteractionKeyMappingTriggered) {
        val player = Minecraft.getInstance().player ?: return

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
        if (player.getSkillTrees() == null) return false
        val hit = Minecraft.getInstance().hitResult ?: return false
        // 如果目标是空气或实体，则无论如何默认进行攻击
        return if (hit.type in listOf(HitResult.Type.ENTITY, HitResult.Type.MISS)) true
        // 如果是方块，则看是否按压时间短于0.15秒，超出则正常挖掘
        else attackKey.getPressTickTime() <= 3
    }

}