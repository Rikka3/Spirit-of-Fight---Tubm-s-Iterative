package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class StunnedAttackPreventionMixin {

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void preventHurtFromStunnedAttacker(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getEntity();
        if (attacker instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"), cancellable = true)
    private void preventSwingWhenStunned(net.minecraft.world.InteractionHand hand, boolean updateSelf, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            ci.cancel();
        }
    }

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V", at = @At("HEAD"), cancellable = true)
    private void preventSwingWhenStunned2(net.minecraft.world.InteractionHand hand, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            ci.cancel();
        }
    }

}
