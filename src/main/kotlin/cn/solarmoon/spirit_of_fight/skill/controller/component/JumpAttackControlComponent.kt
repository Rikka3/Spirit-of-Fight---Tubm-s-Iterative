package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

class JumpAttackControlComponent(
    val jumpAttack: Skill<*>
): SkillControlComponent {

    var jumpContainTick = 0
    override val name: String = "jumpAttack"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (player.jumping) {
            jumpContainTick = -10
        } else jumpContainTick++

        return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
            if (jumpContainTick < 0 && !player.onGround()) {
                jumpContainTick = 0
                preInput.setInput("jump_attack", 5) {
                    jumpAttack.activate()
                    sendPackage(player.id)
                }
                return@onRelease true
            }
            else false
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        jumpAttack.activate()
    }

}