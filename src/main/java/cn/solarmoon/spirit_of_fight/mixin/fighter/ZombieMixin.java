package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.registry.common.SparkRegistries;
import cn.solarmoon.spirit_of_fight.SpiritOfFight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

    private final Zombie zombie = (Zombie) (Object) this;

    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
    private void attack(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!zombie.isPlayingSkill()) {
            var type = level().registryAccess().registry(SparkRegistries.getSKILL_TYPE()).get().get(ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "gloves_combo_0"));
            type.createSkill(zombie, level(), true);
        }
        cir.setReturnValue(false);
    }

}
