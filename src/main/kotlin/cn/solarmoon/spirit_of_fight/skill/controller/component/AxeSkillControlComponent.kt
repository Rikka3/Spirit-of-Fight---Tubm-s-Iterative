package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.skill.concrete.axe.斧子投技
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

class AxeSkillControlComponent(
    val skill: 斧子投技
): SkillControlComponent {

    override val name: String = "axeSkill"

    val holder get() = skill.holder
    val entity get() = skill.entity

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (skill.isActive()) {
            return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
                player.getPreInput().setInput("axe_skill") {
                    if (skill.startAnim.time >= 0.45 && skill.grab != null) {
                        val spirit = entity.getFightSpirit()
                        if (spirit.isFull) {
                            holder.animController.setAnimation(skill.hitAnim, 0)
                            sendPackage(player.id, 0)
                        } else {
                            holder.animController.setAnimation(skill.pullAnim, 0)
                            sendPackage(player.id, 1)
                        }
                    }
                }
                true
            }
        }
        return false
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        when(payload.id) {
            0 -> holder.animController.setAnimation(skill.hitAnim, 0)
            1 -> holder.animController.setAnimation(skill.pullAnim, 0)
        }
    }
}