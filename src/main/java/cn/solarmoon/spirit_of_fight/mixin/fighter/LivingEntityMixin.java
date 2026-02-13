package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private final LivingEntity entity = (LivingEntity) (Object) this;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isImmobile", at = @At("HEAD"), cancellable = true)
    private void preventMovementWhenStunned(CallbackInfoReturnable<Boolean> cir) {
        if (this.entity instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "doPush", at = @At("HEAD"), cancellable = true)
    private void preventPushWhenStunned(Entity entity, CallbackInfo ci) {
        if (this.entity instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            ci.cancel();
        }
    }

//    @Redirect(method = "pushEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getBoundingBox()Lnet/minecraft/world/phys/AABB;"))
//    private AABB test2(LivingEntity instance) {
//        // 防贴脸
//        return entity.getCurrentSkillSet() != null ? getBoundingBox().inflate(0.3) : getBoundingBox();
//    }

}
