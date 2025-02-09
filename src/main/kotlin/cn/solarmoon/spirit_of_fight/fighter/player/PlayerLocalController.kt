package cn.solarmoon.spirit_of_fight.fighter.player

import cn.solarmoon.spark_core.animation.vanilla.asAnimatable
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.local_control.LocalInputController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.controller.SkillGroupLocalController
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent

object PlayerLocalController: LocalInputController() {

    val attackKey get() = Minecraft.getInstance().options.keyAttack

    var moveRemain = 0

    override fun laterInit() {
        addTickingKey(attackKey)
        addTickingKey(SOFKeyMappings.GUARD)
        addTickingKey(SOFKeyMappings.SPECIAL_ATTACK)
    }

    override fun tick(player: LocalPlayer, input: Input) {
        val skillGroup = player.activeSkillGroup ?: return

        skillGroup.controllers.forEach {
            if (it is SkillGroupLocalController) it.localTick(this, player.clientLevel, player, player.getPreInput(), input)
        }

        skillGroup.controllers.takeWhile {
            var next = false
            if (it is SkillGroupLocalController) {
                next = it.localControl(this, player.clientLevel, player, player.getPreInput(), input)
            }
            !next
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
        if (!player.mayFly() && !player.isSwimming && !player.jumping && Minecraft.getInstance().options.keyJump.isDown && player.onGround()) {
            player.getPreInput().setInput("jump", 5) {
                player.jumping = true
                player.jumpFromGround()
            }
        }
    }

    override fun onInteract(player: LocalPlayer, event: InputEvent.InteractionKeyMappingTriggered) {
        if (event.isAttack && shouldSkillAttack(player)) {
            event.setSwingHand(false)
            event.isCanceled = true
        }
//        } else if (player.getTypedSkillController<FightSkillController<*>>()?.isPlaying() == true) {
//            // 使用任何技能时都不能使用物品和交互
//            event.setSwingHand(false)
//            event.isCanceled = true
//        }
    }

    override fun updateMovement(player: LocalPlayer, event: MovementInputUpdateEvent) {

    }

    fun shouldSkillAttack(player: LocalPlayer): Boolean {
        if (player.activeSkillGroup == null) return false
        val hit = Minecraft.getInstance().hitResult ?: return false
        // 如果目标是空气或实体，则无论如何默认进行攻击
        return if (hit.type in listOf(HitResult.Type.ENTITY, HitResult.Type.MISS)) true
        // 如果是方块，则看是否按压时间短于0.15秒，超出则正常挖掘
        else getPressTick(attackKey) <= 3
    }

}