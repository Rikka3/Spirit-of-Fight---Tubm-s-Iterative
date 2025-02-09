package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.skill.SkillGroupControlPayload
import cn.solarmoon.spark_core.skill.SkillGroupController
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

interface SkillGroupLocalController: SkillGroupController {

    fun localControl(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean

    fun localTick(
        controller: PlayerLocalController,
        level: ClientLevel,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ) {}

    override fun sync(host: SkillHost, data: CompoundTag, context: IPayloadContext)

    fun sendServerPackage(host: SkillHost, data: CompoundTag = CompoundTag()) = PacketDistributor.sendToServer(SkillGroupControlPayload(host, id, data))

}