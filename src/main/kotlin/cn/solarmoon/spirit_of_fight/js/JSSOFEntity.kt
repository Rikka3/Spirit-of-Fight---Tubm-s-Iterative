package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.js.extension.JSEntity
import cn.solarmoon.spark_core.registry.common.SparkStateMachineRegister
import cn.solarmoon.spark_core.state_machine.presets.PlayerBaseAnimStateMachine
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import net.minecraft.world.entity.player.Player
import ru.nsk.kstatemachine.statemachine.processEventBlocking

interface JSSOFEntity: JSEntity {

    fun addFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.addStage(value)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun removeFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.removeStage(value)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun getFightSpirit() = entity.getFightSpirit()

    fun clearGrabs() = entity.grabManager.clear()

    fun toggleWieldStyle() {
        WieldStyle.switch(entity)
        if (entity is Player) entity.getStateMachineHandler(SparkStateMachineRegister.PLAYER_BASE_STATE)?.machine?.processEventBlocking(PlayerBaseAnimStateMachine.ResetEvent)
    }

}