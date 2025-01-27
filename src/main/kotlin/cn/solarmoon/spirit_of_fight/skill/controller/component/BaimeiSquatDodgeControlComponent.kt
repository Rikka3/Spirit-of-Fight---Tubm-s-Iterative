package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.skill.concrete.baimei.BaimeiSquatDodge
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

class BaimeiSquatDodgeControlComponent(
    val dodge: BaimeiSquatDodge
): SkillControlComponent {

    override val name: String = "baimeiSquat"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onPressOnce(Minecraft.getInstance().options.keyShift) {
            preInput.setInput("dodge") {
                dodge.activate()
                sendPackage(player.id)
            }
            true
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        dodge.activate()
    }
}