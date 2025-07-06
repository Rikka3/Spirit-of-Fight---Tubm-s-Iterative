package cn.solarmoon.spirit_of_fight.mixin.js;

import cn.solarmoon.spirit_of_fight.js.JSSOFEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements JSSOFEntity {
}
