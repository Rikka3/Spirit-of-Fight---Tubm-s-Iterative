package cn.solarmoon.spirit_of_fight.hit

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import kotlin.random.Random

object EntityHitApplier {

    @SubscribeEvent
    private fun onHurtEffect(event: LivingDamageEvent.Post) {
        val entity = event.entity
        val level = entity.level()
        val data = event.source.extraData ?: return
        val hitSound = data.read<List<SoundEvent>>("hitSound") ?: return

        level.playSound(null, entity.onPos.above(), hitSound.random(), SoundSource.PLAYERS, 1f, 0.9f + Random.nextFloat() / 5)
    }

}