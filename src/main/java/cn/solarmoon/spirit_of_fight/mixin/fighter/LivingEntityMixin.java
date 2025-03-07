package cn.solarmoon.spirit_of_fight.mixin.fighter;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private final LivingEntity entity = (LivingEntity) (Object) this;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "doPush", at = @At("HEAD"), cancellable = true)
    private void test(Entity entity, CallbackInfo ci) {
        if (entity.getGrabManager().getGrabbedBy() != null || this.entity.getGrabManager().getGrabbedBy() != null) ci.cancel();
    }

}
