package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.event.EntityGetWeaponEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent

object AttackModifier {





    @SubscribeEvent
    private fun modifyWeaponHand(event: EntityGetWeaponEvent) {
        val entity = event.entity
    }

    /**
     * 取消默认情况下击退的y轴向量
     */
    @SubscribeEvent
    private fun knockBackModify(event: LivingKnockBackEvent) {
        val entity = event.entity
        entity.setOnGround(false)
    }

}