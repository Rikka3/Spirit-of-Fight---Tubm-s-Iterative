package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.feature.fight_skill.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.IHoldReleaseSkill
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.handling.IPayloadContext

class HoldReleaseSkillControlComponent(
    val skill: IHoldReleaseSkill
): SkillControlComponent {

    override val name: String = "maceSkill"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (!player.getFightSpirit().isFull) return false
        return controller.onPress(SOFKeyMappings.SPECIAL_ATTACK) {
            if ((skill as? Skill<*>)?.isActive() == true) return@onPress false
            preInput.setInput("special_attack", 5) {
                (skill as? Skill<*>)?.activate()
                sendPackage(player.id, 0)
            }
            true
        } || controller.onRelease(SOFKeyMappings.SPECIAL_ATTACK) {
            preInput.setInput("special_attack", 5) {
                skill.releaseCheck = true
                sendPackage(player.id, 1)
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
            0 -> (skill as? Skill<*>)?.activate()
            1 -> skill.releaseCheck = true
        }
    }

}