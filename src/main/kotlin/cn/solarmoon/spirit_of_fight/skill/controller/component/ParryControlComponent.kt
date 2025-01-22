package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.concrete.common.GuardSkill
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ParrySkill
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

open class ParryControlComponent(
    open val guard: GuardSkill,
    open val parry: ParrySkill
): SkillControlComponent {

    override val name: String = "parry"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onPressOnce(SOFKeyMappings.PARRY) {
            if (!guard.isBacking && guard.isActive()) {
                parry.activate()
                sendPackage(player.id)
                true
            }
            false
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        parry.activate()
    }


}