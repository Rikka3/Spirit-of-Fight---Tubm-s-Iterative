package cn.solarmoon.spirit_of_fight.registry.client

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.WarhammerVindicator
import cn.solarmoon.spirit_of_fight.registry.common.SOFEntities
import net.minecraft.client.model.IllagerModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.IllagerRenderer
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@EventBusSubscriber(modid = SpiritOfFight.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object SOFEntityRenderers {

    @SubscribeEvent
    @JvmStatic
    fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(SOFEntities.WARHAMMER_VINDICATOR.get()) { context ->
            WarhammerVindicatorRenderer(context)
        }
    }
}

/**
 * Renderer for the Warhammer Vindicator entity.
 * Uses the vanilla vindicator model (IllagerModel) with a custom texture.
 */
class WarhammerVindicatorRenderer(context: EntityRendererProvider.Context) :
    IllagerRenderer<WarhammerVindicator>(
        context, 
        IllagerModel(context.bakeLayer(ModelLayers.VINDICATOR)), 
        0.5f
    ) {
    
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(
            SpiritOfFight.MOD_ID,
            "textures/entity/warhammer_vindicator.png"
        )
    }
    
    override fun getTextureLocation(entity: WarhammerVindicator): ResourceLocation {
        return TEXTURE
    }
}
