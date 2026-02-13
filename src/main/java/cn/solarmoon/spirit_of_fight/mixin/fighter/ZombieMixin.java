package cn.solarmoon.spirit_of_fight.mixin.fighter;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

    private final Zombie zombie = (Zombie) (Object) this;

    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

//    @Override
//    protected AABB getAttackBoundingBox() {
//        return super.getAttackBoundingBox().inflate(1, 0.0, 1);
//    }

//    @Override
//    public void meleeAttack(@NotNull LivingEntity target) {
////        getPreInput().setInput(SOFPreInputs.ATTACK, 5, 0, () -> {
////            var type = SkillManager.INSTANCE.get(SOFSkillTypes.INSTANCE.getGLOVES_SKILL_0());
////            type.createSkill(zombie, level(), true);
////            return Unit.INSTANCE;
////        });
//    }

}
