package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillComponent
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.box.BoxFollowAnimatedBone
import cn.solarmoon.spirit_of_fight.skill.controller.ComboController
import cn.solarmoon.spirit_of_fight.skill.controller.PreInputCommonReleaseController
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.RegisterEvent

object SOFCodecRegister {

    private fun reg(event: RegisterEvent) {
        event.register(SOFRegistries.BOX_GENERATION_TYPE_CODEC.key(), id("follow_with_animated_bone")) { BoxFollowAnimatedBone.CODEC }

        fun registerSkillComponent(id: String, codec: () -> MapCodec<out SkillComponent>) = event.register(SparkRegistries.SKILL_COMPONENT_CODEC.key(), id(id), codec)
        registerSkillComponent("anim_box_attack") { AnimBoxAttackComponent.CODEC }

        fun registerSkillGroupController(id: String, codec: () -> MapCodec<out SkillGroupController>) = event.register(SparkRegistries.SKILL_GROUP_CONTROLLER_CODEC.key(), id(id), codec)
        registerSkillGroupController("combo") { ComboController.CODEC }
        registerSkillGroupController("preinput_common_release") { PreInputCommonReleaseController.CODEC }
    }

    private fun id(id: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id)

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}