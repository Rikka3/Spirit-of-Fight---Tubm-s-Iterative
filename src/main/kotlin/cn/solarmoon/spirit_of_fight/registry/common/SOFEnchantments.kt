package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Enchantments in Minecraft 1.21.1 are data-driven and don't require Kotlin code.
 * The Spikey enchantment is defined in data/spirit_of_fight/enchantment/spikey.json
 * This class exists to ensure the enchantment registry is loaded.
 */
object SOFEnchantments {
    private val ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, SpiritOfFight.MOD_ID)

    @JvmStatic
    fun register(bus: IEventBus) {
        ENCHANTMENTS.register(bus)
    }
}
