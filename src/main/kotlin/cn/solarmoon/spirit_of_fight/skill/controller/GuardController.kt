package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.getSkillType
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
import net.neoforged.neoforge.network.handling.IPayloadContext

class GuardController(
    override val skills: List<ResourceLocation>
): SkillGroupLocalController {

    val guardIndex get() = skills[0]

    var guard: SkillInstance? = null

    override fun localControl(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        if (!SOFKeyMappings.GUARD.isDown) {
            if (guard?.isActive == true) {
                preInput.setInput("guard_stop") {
                    guard?.end()
                    sendServerPackage(player, CompoundTag().apply { putInt("op", 1) })
                }
                return true
            }
        }

        return controller.onPress(SOFKeyMappings.GUARD) {
            if (guard?.isActive == true) return@onPress false

            // 防止和使用物品冲突
            Minecraft.getInstance().options.keyUse.isDown = false

            preInput.setInput("guard", 5) {
                guard = getSkillType(level, guardIndex).createSkill(player, level)
                guard?.activate()
                sendServerPackage(player, CompoundTag().apply { putInt("op", 0) })
            }
            true
        }
    }

    override fun sync(
        host: SkillHost,
        data: CompoundTag,
        context: IPayloadContext
    ) {
        val level = context.player().level()
        when(data.getInt("op")) {
            0 -> {
                guard = getSkillType(level, guardIndex).createSkill(host, level)
                guard?.activate()
            }
            1 -> guard?.end()
        }
    }

    override val codec: MapCodec<out SkillGroupController> = CODEC

    companion object {
        val CODEC: MapCodec<GuardController> = RecordCodecBuilder.mapCodec {
            it.group(
                ResourceLocation.CODEC.listOf().fieldOf("skills").forGetter { it.skills }
            ).apply(it, ::GuardController)
        }
    }

}