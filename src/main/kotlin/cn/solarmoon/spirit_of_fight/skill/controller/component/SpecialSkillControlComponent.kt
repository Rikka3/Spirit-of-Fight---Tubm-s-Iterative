package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

open class SpecialSkillControlComponent(
    val special: Skill<*>,
    val needFullSpirit: Boolean
): SkillControlComponent {

    override val name = "specialSkill"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onPressOnce(SOFKeyMappings.SPECIAL_ATTACK) {
            if (needFullSpirit && !player.getFightSpirit().isFull) return@onPressOnce false

            preInput.setInput("special_attack", 5) {
                special.activate()
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
        special.activate()
    }


}