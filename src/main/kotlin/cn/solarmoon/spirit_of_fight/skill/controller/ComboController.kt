package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.getSkillType
import cn.solarmoon.spark_core.util.CycleIndex
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

class ComboController(
    override val skills: List<ResourceLocation>
): SkillGroupLocalController {

    val maxComboAmount = skills.size
    val index = CycleIndex(maxComboAmount, maxComboAmount - 1)
    val presentComboSkillId get() = skills[index.get()]
    var tickRemain = 0
    var presentCombo: SkillInstance? = null

    override val codec: MapCodec<out SkillGroupController> = CODEC

    override fun localControl(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onRelease(Minecraft.getInstance().options.keyAttack) {
            if (tickRemain == 0) index.set(maxComboAmount - 1)
            if (it > 3) return@onRelease false
            tickRemain = 20
            preInput.setInput("combo", 5) {
                index.increment()
                presentCombo = getSkillType(level, presentComboSkillId).createSkill(player, level, true)
                sendServerPackage(player, CompoundTag().apply { putInt("ComboIndex", index.get()) })
            }
            true
        }
    }

    override fun localTick(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ) {
        if (tickRemain > 0 && presentCombo?.isActive == false) tickRemain--
    }

    override fun sync(host: SkillHost, data: CompoundTag, context: IPayloadContext) {
        index.set(data.getInt("ComboIndex"))
    }

    companion object {
        val CODEC: MapCodec<ComboController> = RecordCodecBuilder.mapCodec {
            it.group(
                ResourceLocation.CODEC.listOf().fieldOf("skills").forGetter { it.skills }
            ).apply(it, ::ComboController)
        }
    }

}