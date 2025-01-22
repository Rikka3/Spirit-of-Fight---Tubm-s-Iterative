package cn.solarmoon.spirit_of_fight.feature.fight_skill.sync

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.skill.controller.getTypedSkillController
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.controller.FightSkillController
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

data class ClientOperationPayload(
    val entityId: Int,
    val operation: String,
    val moveVector: Vec3,
    val id: Int
): CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

    companion object {
        @JvmStatic
        fun handle(payload: ClientOperationPayload, context: IPayloadContext) {
            val player = context.player()
            val level = player.level()
            val entity = level.getEntity(payload.entityId) ?: return
            val skillController = entity.getTypedSkillController<FightSkillController<*>>() ?: return

            skillController.allComponents.forEach {
                if (payload.operation == it.name) it.serverControl(entity, payload, context)
            }

            when(payload.operation) {
//                "parried_left" -> {
//                    val attacker = level.getEntity(payload.id) ?: return
//                    if (attacker !is IEntityAnimatable<*>) return
//                    skillController.skill<ParrySkill>(SkillId.PARRY)?.playParriedAnim(Side.LEFT, attacker)
//                }
//                "parried_right" -> {
//                    val attacker = level.getEntity(payload.id) ?: return
//                    if (attacker !is IEntityAnimatable<*>) return
//                    skillController.skill<ParrySkill>(SkillId.PARRY)?.playParriedAnim(Side.RIGHT, attacker)
//                }
            }

            // 由于技能默认不会强行覆盖正在进行中的技能，所以此处可以再发回原客户端以在某些情况下修正动画（暂时没这么做，因为还没碰到需要修正的情况）
            if (player is ServerPlayer) PacketDistributor.sendToPlayersNear(player.serverLevel(), player, player.x, player.y, player.z, 512.0, payload)
        }

        @JvmStatic
        val TYPE = CustomPacketPayload.Type<ClientOperationPayload>(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "client_operation"))

        @JvmStatic
        val STREAM_CODEC = object : StreamCodec<RegistryFriendlyByteBuf, ClientOperationPayload> {
            override fun decode(buffer: RegistryFriendlyByteBuf): ClientOperationPayload {
                val entityId = buffer.readInt()
                val operation = buffer.readUtf()
                val moveVector = SerializeHelper.VEC3_STREAM_CODEC.decode(buffer)
                val moveDirection = buffer.readInt()
                return ClientOperationPayload(entityId, operation, moveVector, moveDirection)
            }

            override fun encode(buffer: RegistryFriendlyByteBuf, value: ClientOperationPayload) {
                buffer.writeInt(value.entityId)
                buffer.writeUtf(value.operation)
                SerializeHelper.VEC3_STREAM_CODEC.encode(buffer, value.moveVector)
                buffer.writeInt(value.id)
            }
        }
    }

}