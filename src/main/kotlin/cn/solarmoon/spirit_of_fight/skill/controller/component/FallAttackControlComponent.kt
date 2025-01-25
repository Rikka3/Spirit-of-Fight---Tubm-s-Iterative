package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.isFalling
import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

class FallAttackControlComponent(
    val skill: Skill<*>
): SkillControlComponent {

    override val name: String = "fallAttack"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
            if (player.isFalling() && player.fallDistance > 4) {
                preInput.setInput("fall_attack", 5) {
                    skill.activate()
                    sendPackage(player.id)
                }
                return@onRelease true
            }
            false
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        skill.activate()
    }

}