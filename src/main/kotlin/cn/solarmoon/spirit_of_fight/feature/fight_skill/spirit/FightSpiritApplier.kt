package cn.solarmoon.spirit_of_fight.feature.fight_skill.spirit

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

object FightSpiritApplier {

    @SubscribeEvent
    private fun entityTick(event: EntityTickEvent.Post) { // 战意作为结算性质的机制需要在结尾tick而非开头，否则可能会比攻击先触发导致下一个tick才会进行当前tick攻击后应有的结果
        val entity = event.entity
        val fs = entity.getFightSpirit()
        fs.tick()
    }

    @SubscribeEvent
    private fun onJoinServer(event: EntityJoinLevelEvent) {
        val level = event.level
        if (level.isClientSide) return
        val player = event.entity
        player.getFightSpirit().syncToClient(player.id)
    }

}