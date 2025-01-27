package cn.solarmoon.spirit_of_fight.fighter.player

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.preset_anim.PlayerStateAnimMachine
import cn.solarmoon.spark_core.animation.preset_anim.getStateMachine
import cn.solarmoon.spark_core.animation.vanilla.asAnimatable
import cn.solarmoon.spark_core.entity.getInputVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.local_control.LocalInputController
import cn.solarmoon.spark_core.registry.common.SparkTypedAnimations
import cn.solarmoon.spark_core.skill.controller.getTypedSkillController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.controller.FightSkillController
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import ru.nsk.kstatemachine.statemachine.processEventBlocking

object PlayerLocalController: LocalInputController() {

    val attackKey get() = Minecraft.getInstance().options.keyAttack

    var moveRemain = 0

    override fun laterInit() {
        addTickingKey(attackKey)
        addTickingKey(SOFKeyMappings.GUARD)
        addTickingKey(SOFKeyMappings.SPECIAL_ATTACK)
    }

    override fun tick(player: LocalPlayer, input: Input) {
        val skillController = player.getTypedSkillController<FightSkillController<*>>() ?: return

        if (input.moveVector.length() > 0 && !player.getPreInput().hasInput() && skillController.isPlaying()) {
            player.getPreInput().setInput("move") {
                moveRemain = 10
                player.asAnimatable().animController.stopAnimation()
            }
        }

        if (moveRemain > 0) moveRemain--

        skillController.allComponents.any {
            it.localControl(this, player, player.getPreInput(), input)
        }

        // 把跳跃加入预输入防止卡手
        if (!player.mayFly() && !player.isSwimming && !player.jumping && Minecraft.getInstance().options.keyJump.isDown && player.onGround()) {
            player.getPreInput().setInput("jump") {
                player.jumping = true
                player.jumpFromGround()
            }
        }

        // 不在进行任何技能时可释放预输入
        if (!skillController.isPlaying()) {
            player.getPreInput().executeIfPresent()
        }
    }

    override fun onInteract(player: LocalPlayer, event: InputEvent.InteractionKeyMappingTriggered) {
        if (event.isAttack && shouldAttack(player)) {
            event.setSwingHand(false)
            event.isCanceled = true
        } else if (player.getTypedSkillController<FightSkillController<*>>()?.isPlaying() == true) {
            // 使用任何技能时都不能使用物品和交互
            event.setSwingHand(false)
            event.isCanceled = true
        }
    }

    override fun updateMovement(player: LocalPlayer, event: MovementInputUpdateEvent) {
        val controller = player.getTypedSkillController<FightSkillController<*>>() ?: return
        controller.allComponents.forEach { it.updateMovement(player, player.input, event) }
    }

    fun shouldAttack(player: LocalPlayer): Boolean {
        player.getTypedSkillController<FightSkillController<*>>() ?: return false
        val hit = Minecraft.getInstance().hitResult ?: return false
        // 如果目标是空气或实体，则无论如何默认进行攻击
        return if (hit.type in listOf(HitResult.Type.ENTITY, HitResult.Type.MISS)) true
        // 如果是方块，则看是否按压时间短于0.25秒，超出则正常挖掘
        else getPressTick(attackKey) < 5
    }

}