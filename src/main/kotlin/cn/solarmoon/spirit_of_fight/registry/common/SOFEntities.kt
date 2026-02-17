package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.WarhammerVindicator
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object SOFEntities {
    
    private val ENTITIES: DeferredRegister<EntityType<*>> = DeferredRegister.create(Registries.ENTITY_TYPE, SpiritOfFight.MOD_ID)
    
    @JvmStatic
    val WARHAMMER_VINDICATOR: DeferredHolder<EntityType<*>, EntityType<WarhammerVindicator>> = ENTITIES.register(
        "warhammer_vindicator",
        Supplier {
            EntityType.Builder.of(::WarhammerVindicator, MobCategory.MONSTER)
                .sized(0.6f, 1.95f)
                .clientTrackingRange(8)
                .updateInterval(3)
                .build(SpiritOfFight.MOD_ID + ":warhammer_vindicator")
        }
    )
    
    @JvmStatic
    val WARHAMMER_VINDICATOR_SPAWN_EGG = SpiritOfFight.REGISTER.item<SpawnEggItem>()
        .id("warhammer_vindicator_spawn_egg")
        .bound { SpawnEggItem(WARHAMMER_VINDICATOR.get(), 0x483E3E, 0x8B8B8B, Item.Properties()) }
        .build()
    
    @JvmStatic
    fun register(modEventBus: IEventBus) {
        ENTITIES.register(modEventBus)
        
        // Register entity attributes using EntityAttributeCreationEvent for custom entities
        modEventBus.addListener(this::registerEntityAttributes)
    }
    
    private fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
        // Register the complete attribute supplier for Warhammer Vindicator
        // This includes all base attributes from Vindicator plus our custom modifications
        event.put(WARHAMMER_VINDICATOR.get(), WarhammerVindicator.createAttributes().build())
    }
}
