package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldGuardSkill
import cn.solarmoon.spirit_of_fight.skill.concrete.common.ShieldParrySkill
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.common.Tags

class ShieldParryControlComponent(
    guard: ShieldGuardSkill,
    parry: ShieldParrySkill,
    private val isHoldingShield: LivingEntity.() -> Boolean = { offhandItem.`is`(Tags.Items.TOOLS_SHIELD) }
): ParryControlComponent(guard, parry) {

    fun isHoldingShield(entity: LivingEntity) = isHoldingShield.invoke(entity)

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (!isHoldingShield(player)) return false
        return super.localControl(controller, player, preInput, input)
    }

}