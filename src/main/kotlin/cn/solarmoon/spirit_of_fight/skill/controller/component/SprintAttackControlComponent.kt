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

class SprintAttackControlComponent(
    val sprintingAttack: Skill<*>
): SkillControlComponent {

    override val name: String = "sprintAttack"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
            if (it >= 5) return@onRelease false
            if (player.isSprinting) {
                preInput.setInput("sprinting_attack", 5) {
                    sprintingAttack.activate()
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
        sprintingAttack.activate()
    }


}