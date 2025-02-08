package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spark_core.skill.getSkillType
import cn.solarmoon.spark_core.util.CycleIndex
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

class ComboController(
    override val skills: List<ResourceLocation>
): SkillGroupLocalController {

    val maxComboAmount = skills.size
    val index = CycleIndex(maxComboAmount, maxComboAmount - 1)
    val presentComboSkill get() = skills[index.get()]

    override val codec: MapCodec<out SkillGroupController> = CODEC

    override fun localControl(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
            preInput.setInput("combo", 5) {
                index.increment()
                getSkillType(level, presentComboSkill).createSkill(player).activate()
                sendServerPackage(player, CompoundTag().apply { putInt("ComboIndex", index.get()) })
            }
            true
        }
    }

    override fun sync(host: SkillHost, data: CompoundTag, context: IPayloadContext) {
        index.set(data.getInt("ComboIndex"))
        getSkillType(context.player().level(), presentComboSkill).createSkill(host).activate()
    }

    companion object {
        val CODEC: MapCodec<ComboController> = RecordCodecBuilder.mapCodec {
            it.group(
                ResourceLocation.CODEC.listOf().fieldOf("skills").forGetter { it.skills }
            ).apply(it, ::ComboController)
        }
    }

}