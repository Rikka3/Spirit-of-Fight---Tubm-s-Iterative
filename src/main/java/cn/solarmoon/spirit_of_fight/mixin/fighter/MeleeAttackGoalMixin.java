package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.ICustomMeleeAttackEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MeleeAttackGoal.class)
public class MeleeAttackGoalMixin {

    @Shadow @Final protected PathfinderMob mob;

    @Inject(method = "checkAndPerformAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;doHurtTarget(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    private void check(LivingEntity target, CallbackInfo ci) {
        if (mob instanceof ICustomMeleeAttackEntity entity) {
            entity.meleeAttack(target);
            ci.cancel();
        }
    }

}
