package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * 在每个控制器中按顺序排序以触发优先级
 */
interface SkillControlComponent {

    val name: String

    /**
     * @return 决定是否在此中断，false则继续执行判断后面的控制组件
     */
    fun localControl(controller: PlayerLocalController, player: LocalPlayer, preInput: PreInput, input: Input): Boolean

    fun serverControl(entity: Entity, payload: ClientOperationPayload, context: IPayloadContext)

    fun tick(controller: FightSkillController<*>) {}

    fun updateMovement(player: LocalPlayer, input: Input, event: MovementInputUpdateEvent) {}

    fun sendPackage(entityId: Int, id: Int = 0, v: Vec3 = Vec3.ZERO) {
        PacketDistributor.sendToServer(ClientOperationPayload(entityId, name, v, id))
    }

}