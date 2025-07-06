package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.registry.common.SparkRegistries;
import cn.solarmoon.spark_core.skill.SkillManager;
import cn.solarmoon.spirit_of_fight.SpiritOfFight;
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes;
import cn.solarmoon.spirit_of_fight.data.skills.VindicatorSkill;
import cn.solarmoon.spirit_of_fight.entity.ICustomMeleeAttackEntity;
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs;
import kotlin.Unit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements ICustomMeleeAttackEntity {

    private final Zombie zombie = (Zombie) (Object) this;

    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(1, 0.0, 1);
    }

    @Override
    public void meleeAttack(@NotNull LivingEntity target) {
        getPreInput().setInput(SOFPreInputs.ATTACK, 5, 0, () -> {
            var type = SkillManager.INSTANCE.get(SOFSkillTypes.INSTANCE.getGLOVES_SKILL_0());
            type.createSkill(zombie, level(), true);
            return Unit.INSTANCE;
        });
    }

}
