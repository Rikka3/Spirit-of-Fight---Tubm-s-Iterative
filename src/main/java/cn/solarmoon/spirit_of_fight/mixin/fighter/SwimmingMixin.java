package cn.solarmoon.spirit_of_fight.mixin.fighter;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to prevent swimming animation unless the player is sprinting.
 * This ensures players in water don't automatically go into swimming pose
 * unless they are actively sprinting.
 */
@Mixin(Entity.class)
public class SwimmingMixin {

    @Inject(method = "isSwimming", at = @At("RETURN"), cancellable = true)
    private void preventSwimIfNotSprinting(CallbackInfoReturnable<Boolean> cir) {
        // Only modify for players
        if (cir.getReturnValue() && (Object) this instanceof Player player) {
            // If the player is not sprinting, prevent swimming animation
            if (!player.isSprinting()) {
                cir.setReturnValue(false);
            }
        }
    }
}
