package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import net.minecraft.data.PackOutput
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider
import net.neoforged.neoforge.registries.DeferredHolder

class SOFSoundProvider(
    output: PackOutput,
    helper: ExistingFileHelper
): SoundDefinitionsProvider(output, SpiritOfFight.MOD_ID, helper) {

    override fun registerSounds() {
        addDefault(SOFSounds.SHARP_BLOCK)
        addDefault(SOFSounds.SHARP_PARRY_1)
        addDefault(SOFSounds.SHARP_PARRY_2)
        addDefault(SOFSounds.SHARP_PARRY_3)
        addDefault(SOFSounds.SHARP_WIELD_1)
        addDefault(SOFSounds.SHARP_UNDER_ATTACK_1)
        addDefault(SOFSounds.SHARP_UNDER_ATTACK_2)
        addDefault(SOFSounds.SHARP_UNDER_ATTACK_3)
        addDefault(SOFSounds.SHARP_UNDER_ATTACK_4)
        addDefault(SOFSounds.SHARP_UNDER_ATTACK_5)
        addDefault(SOFSounds.HARD_BLOCK)
        addDefault(SOFSounds.HARD_WIELD_1)
        addDefault(SOFSounds.HARD_UNDER_ATTACK_1)
        addDefault(SOFSounds.HARD_UNDER_ATTACK_2)
        addDefault(SOFSounds.SOFT_BLOCK)
        addDefault(SOFSounds.SOFT_UNDER_ATTACK_1)
        addDefault(SOFSounds.SOFT_UNDER_ATTACK_2)
        addDefault(SOFSounds.SOFT_UNDER_ATTACK_3)
        addDefault(SOFSounds.SOFT_UNDER_ATTACK_4)
        addDefault(SOFSounds.PERFECT_DODGE)
        addDefault(SOFSounds.WOODEN_FISH)
    }

    fun addDefault(event: DeferredHolder<SoundEvent, SoundEvent>, volume: Float = 1.0f) {
        val id = event.id.path.replace('.', '/')
        add(event.get(), SoundDefinition.definition()
            .with(
                sound("${SpiritOfFight.MOD_ID}:$id", SoundDefinition.SoundType.SOUND)
                    .volume(volume)
            )
            .subtitle("sound.${SpiritOfFight.MOD_ID}.$id")
            .replace(true))
    }


}