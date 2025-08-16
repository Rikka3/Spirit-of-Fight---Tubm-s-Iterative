package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.skill.SkillManager;
import cn.solarmoon.spirit_of_fight.data.skills.VindicatorSkill;
import cn.solarmoon.spirit_of_fight.entity.ICustomMeleeAttackEntity;
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs;
import kotlin.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Vindicator.class)
public abstract class VindicatorMixin extends AbstractIllager implements ICustomMeleeAttackEntity {

    @Shadow public abstract IllagerArmPose getArmPose();

    private final Vindicator vindicator = (Vindicator) (Object) this;

    protected VindicatorMixin(EntityType<? extends AbstractIllager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(1.0, 0.0, 1.0);
    }

    @Override
    public void meleeAttack(@NotNull LivingEntity target) {
        getPreInput().setInput(SOFPreInputs.ATTACK, 5, () -> {
            var type = SkillManager.INSTANCE.get(VindicatorSkill.INSTANCE.random());
            type.createSkill(vindicator, level(), true);
            return Unit.INSTANCE;
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (vindicator.isPlayingSkill()) {
            // 保证斧子抽出
            setAggressive(true);
        }
    }
}
