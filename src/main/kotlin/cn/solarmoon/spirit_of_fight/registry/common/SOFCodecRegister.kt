package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.component.PerfectDodgeComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.collision.CommonAttackCollisionComponent
import cn.solarmoon.spirit_of_fight.skill.controller.ComboController
import cn.solarmoon.spirit_of_fight.skill.controller.DodgeController
import cn.solarmoon.spirit_of_fight.skill.controller.PreInputCommonReleaseController
import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.RegisterEvent

object SOFCodecRegister {

    private fun reg(event: RegisterEvent) {
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("common_attack")) { CommonAttackCollisionComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("prevent_move_with_back")) { PreventMoveWithBackComponent.CODEC }
        event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id("perfect_dodge")) { PerfectDodgeComponent.CODEC }

        fun registerSkillGroupController(id: String, codec: () -> MapCodec<out SkillGroupController>) = event.register(SparkRegistries.SKILL_GROUP_CONTROLLER_CODEC.key(), id(id), codec)
        registerSkillGroupController("combo") { ComboController.CODEC }
        registerSkillGroupController("dodge") { DodgeController.CODEC }
        registerSkillGroupController("preinput_common_release") { PreInputCommonReleaseController.CODEC }
    }

    private fun id(id: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id)

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}