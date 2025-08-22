package cn.solarmoon.spirit_of_fight.poise_system

import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.network.PacketDistributor

class PoiseData(
    val entity: Entity,
    var currentValue: Int = 100,
    var maxValue: Int = 100,
) {

    val isBroken get() = currentValue <= 0

    fun reduce(value: Int): Boolean {
        if (!entity.level().isClientSide) {
            currentValue -= value
            syncToClient()
            if (isBroken) {
                reset()
                resetToClient()
                return true
            }
        }
        return false
    }

    fun reset() {
        currentValue = maxValue
    }

    fun syncToClient() {
        PacketDistributor.sendToAllPlayers(PoiseSetPayload(entity.id, currentValue, maxValue))
    }

    fun resetToClient() {
        PacketDistributor.sendToAllPlayers(PoiseResetPayload(entity.id))
    }

    fun initValue(value: Int) {
        currentValue = value
        maxValue = value
    }

}