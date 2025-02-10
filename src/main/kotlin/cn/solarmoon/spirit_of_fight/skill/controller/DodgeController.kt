package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spark_core.skill.getSkillType
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

class DodgeController(
    override val skills: List<ResourceLocation>
): SkillGroupLocalController {

    val dodge get() = skills[0]

    override val codec: MapCodec<out SkillGroupController> = CODEC

    override fun localControl(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onPressOnce(SOFKeyMappings.DODGE) {
            if (player.onGround()) {
                preInput.setInput("dodge", 5) {
                    getSkillType(level, dodge).createSkill(player, level).activate()
                    sendServerPackage(player)
                }
                return@onPressOnce true
            }
            else false
        }
    }

    override fun sync(
        host: SkillHost,
        data: CompoundTag,
        context: IPayloadContext
    ) {
        val level = context.player().level()
        getSkillType(level, dodge).createSkill(host, level).activate()
    }

    companion object {
        val CODEC: MapCodec<DodgeController> = RecordCodecBuilder.mapCodec {
            it.group(
                ResourceLocation.CODEC.listOf().fieldOf("skills").forGetter { it.skills }
            ).apply(it, ::DodgeController)
        }
    }

}