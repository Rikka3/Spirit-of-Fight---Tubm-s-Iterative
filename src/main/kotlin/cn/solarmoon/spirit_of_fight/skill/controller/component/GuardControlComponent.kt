package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.network.handling.IPayloadContext

open class GuardControlComponent(
    open val guard: GuardSkill
): SkillControlComponent {

    override val name: String = "guard"

    var guardContainTick = 0

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (!SOFKeyMappings.GUARD.isDown) {
            guardContainTick++
            if (guardContainTick > 0) {
                if (guard.isStanding) {
                    preInput.setInput("guard_stop") {
                        guard.end()
                        sendPackage(player.id, 1)
                    }
                    return true
                }
            }
        } else guardContainTick = -3

        return controller.onPress(SOFKeyMappings.GUARD) {
            if (guard.isActive()) return@onPress false

            preInput.setInput("guard", 5) {
                guard.activate()
                sendPackage(player.id, 0)
            }
            true
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        when(payload.id) {
            0 -> guard.activate()
            1 -> guard.end()
        }
    }

    override fun updateMovement(player: LocalPlayer, input: Input, event: MovementInputUpdateEvent) {
        // 格挡时缓慢行走
        if (guard.isStanding == true) {
            input.forwardImpulse /= 4f
            input.leftImpulse /= 4f
            input.jumping = false
            input.shiftKeyDown = false
            player.sprintTriggerTime = -1
            player.swinging = false
        }
    }

}