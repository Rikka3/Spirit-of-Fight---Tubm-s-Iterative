package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
    private void preventAttackWhenStunned(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
