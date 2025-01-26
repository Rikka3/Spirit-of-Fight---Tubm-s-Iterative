package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.util.CycleIndex
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.skill.controller.FightSkillController
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.network.handling.IPayloadContext

open class ComboControlComponent(
    open vararg val comboList: Skill<*>
): SkillControlComponent {

    override val name: String = "combo"
    val maxComboAmount = comboList.size
    val index = CycleIndex(maxComboAmount, maxComboAmount - 1)
    val combo get() = comboList[index.get()]
    var indexRemain = 0

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
            preInput.setInput("combo", 5) {
                index.increment()
                combo.activate()
                sendPackage(player.id, index.get())
            }
            true
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        index.set(payload.id)
        combo.activate()
    }

    override fun tick(controller: FightSkillController<*>) {
        // 不在播放任意技能时重置连击
        if (!controller.isPlaying() && indexRemain == 0) {
            index.set(maxComboAmount - 1)
        }

        if (combo.isActive()) {
            indexRemain = 10
        } else if (indexRemain > 0) indexRemain--
    }

    override fun updateMovement(player: LocalPlayer, input: Input, event: MovementInputUpdateEvent) {
        // 在普通连招过程中可以按住s阻止前移
        if (input.forwardImpulse < 0 && combo.isActive()) {
            player.deltaMovement = Vec3(0.0, player.deltaMovement.y, 0.0)
        }
    }

}