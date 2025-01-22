package cn.solarmoon.spirit_of_fight.fighter

import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

object PatchApplier {

    @SubscribeEvent(priority = EventPriority.LOW)
    private fun join(event: EntityJoinLevelEvent) {
        val entity = event.entity
        val level = event.level
        entity.getPatch().onJoinLevel(level)
    }

}