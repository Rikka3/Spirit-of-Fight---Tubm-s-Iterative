package cn.solarmoon.spirit_of_fight;

import cn.solarmoon.spark_core.entry_builder.ObjectRegister;
import cn.solarmoon.spark_core.resource.common.MultiModResourceRegistry;
import cn.solarmoon.spirit_of_fight.registry.client.*;
import cn.solarmoon.spirit_of_fight.registry.common.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SpiritOfFight.MOD_ID)
public class SpiritOfFight {

    public static final String MOD_ID = "spirit_of_fight";
    public static final Logger LOGGER = LoggerFactory.getLogger("战魂");
    public static final ObjectRegister REGISTER = new ObjectRegister(MOD_ID, false);

    public SpiritOfFight(IEventBus modEventBus, ModContainer modContainer) {
        // 首先注册Spirit of Fight到多mod资源系统
        MultiModResourceRegistry.INSTANCE.registerModResources(MOD_ID, SpiritOfFight.class);

        REGISTER.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            SOFClientEventRegister.register();
            SOFKeyMappings.register();
            SOFGuiRegister.register(modEventBus);
            SOFItemInHandModelRegister.register(modEventBus);
            SOFItemBakedModelModifier.register(modEventBus);
            SOFClientExtensionsRegister.register(modEventBus);
        }

        SOFRegistries.register();
        SOFCodecRegister.register(modEventBus);
        SOFCreativeTab.register(modEventBus);
        SOFItems.register();
        SOFAttachments.register();
        SOFCommonEventRegister.register();
        SOFVisualEffects.register();
        SOFTypedAnimations.register();
        SOFDataGenerater.register(modEventBus);
        SOFPayloadRegister.register(modEventBus);
        SOFSounds.register();
        SOFDataComponents.register();
        SOFCapabilities.register(modEventBus);
        SOFDataRegistryRegister.register(modEventBus);
        SOFMolangQueryRegister.register(modEventBus);
        SOFJSApiRegister.register();
        SOFSkills.register();

    }

}
