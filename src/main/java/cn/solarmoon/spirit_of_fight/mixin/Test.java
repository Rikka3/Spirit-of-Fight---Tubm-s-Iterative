package cn.solarmoon.spirit_of_fight.mixin;

import cn.solarmoon.spark_core.camera.CameraAdjusterKt;
import cn.solarmoon.spirit_of_fight.lock_on.LockOnController;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CameraAdjusterKt.class)
public class Test {

    @Inject(method = "setCameraLock", at = @At("HEAD"), cancellable = true)
    private static void set(Entity $this$setCameraLock, boolean b, CallbackInfo ci) {
        if (LockOnController.getHasTarget() && b) {
            ci.cancel();
        }
    }

}
