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
        addDefault(SOFSounds.SOFT_BLOCK)
        addDefault(SOFSounds.SHARP_BLOCK)
        addDefault(SOFSounds.SHARP_PARRY)
        addDefault(SOFSounds.HARD_BLOCK)
    }

    fun addDefault(event: DeferredHolder<SoundEvent, SoundEvent>) {
        val id = event.id.path
        add(event.get(), SoundDefinition.definition()
            .with(
                sound("${SpiritOfFight.MOD_ID}:$id", SoundDefinition.SoundType.SOUND)
                    .stream(true)
                    .preload(true)
            )
            .subtitle("sound.${SpiritOfFight.MOD_ID}.$id")
            .replace(true))
    }

}