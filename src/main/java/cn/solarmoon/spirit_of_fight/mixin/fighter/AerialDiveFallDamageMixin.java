package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to prevent fall damage when the player is performing an aerial dive attack.
 */
@Mixin(LivingEntity.class)
public abstract class AerialDiveFallDamageMixin {

    /**
     * Cancels fall damage when the entity is aerial diving.
     * The causeFallDamage method signature in 1.21.1 is:
     * boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource)
     */
    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void preventAerialDiveFallDamage(float fallDistance, float multiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof IEntityPatch patch) {
            if (patch.isAerialDiving()) {
                cir.setReturnValue(false);
            }
        }
    }
}
