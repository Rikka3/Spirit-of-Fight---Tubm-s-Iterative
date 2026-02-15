package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.data.SOFBlockTags
import cn.solarmoon.spirit_of_fight.data.SOFItemModelProvider
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.data.SOFLanguageProvider
import cn.solarmoon.spirit_of_fight.data.SOFLanguageProviderEN
import cn.solarmoon.spirit_of_fight.data.SOFRecipeProvider
import cn.solarmoon.spirit_of_fight.data.SOFSoundProvider
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.DataProvider
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.data.event.GatherDataEvent


object SOFDataGenerater {

    @SubscribeEvent
    private fun gather(event: GatherDataEvent) {
        val generator = event.generator
        val output = generator.packOutput
        val lookupProvider = event.lookupProvider
        val helper = event.existingFileHelper

        fun addProvider(provider: DataProvider) = generator.addProvider(event.includeServer(), provider)

        val blockTags = SOFBlockTags(output, lookupProvider, helper)
        addProvider(blockTags)
        addProvider(SOFItemTags(output, lookupProvider, blockTags.contentsGetter(), helper))
        addProvider(SOFLanguageProvider(output))
        addProvider(SOFLanguageProviderEN(output))
        addProvider(SOFRecipeProvider(output, lookupProvider))
        generator.addProvider(event.includeClient(), SOFItemModelProvider(output, helper))
        generator.addProvider(event.includeClient(), SOFSoundProvider(output, helper))

        val builder = RegistrySetBuilder().apply {
            SOFSkillTrees(this)
        }
        generator.addProvider(
            event.includeServer(),
            DataProvider.Factory { DatapackBuiltinEntriesProvider(
                it,
                lookupProvider,
                builder,
                setOf(SpiritOfFight.MOD_ID)
            ) }
        )
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::gather)
    }

}