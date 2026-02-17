package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.skill.SkillManager;
import cn.solarmoon.spark_core.skill.SkillType;
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes;
import cn.solarmoon.spirit_of_fight.entity.ICustomMeleeAttackEntity;
import cn.solarmoon.spirit_of_fight.entity.WarhammerVindicator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WarhammerVindicator.class)
public abstract class WarhammerVindicatorMixin extends Vindicator implements ICustomMeleeAttackEntity {

    private final WarhammerVindicator warhammerVindicator = (WarhammerVindicator) (Object) this;

    protected WarhammerVindicatorMixin(EntityType<? extends Vindicator> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void meleeAttack(@NotNull LivingEntity target) {
        // Use the skill system for attacks instead of vanilla doHurtTarget
        SkillType<?> skillType = getComboSkill();
        if (skillType != null) {
            skillType.createSkill(warhammerVindicator, level(), true);
            // Increment combo count after starting skill
            int currentCombo = warhammerVindicator.getComboCount();
            warhammerVindicator.setComboCount(currentCombo + 1);
            warhammerVindicator.setLastAttackTick(level().getGameTime());
        }
    }

    @Unique
    private SkillType<?> getComboSkill() {
        // Select hammer combo skill based on combo count (cycles through 1-2-3)
        int combo = warhammerVindicator.getComboCount() % 3;
        return switch (combo) {
            case 0 -> SkillManager.INSTANCE.get(SOFSkillTypes.INSTANCE.getHAMMER_DEFAULT_COMBO_1());
            case 1 -> SkillManager.INSTANCE.get(SOFSkillTypes.INSTANCE.getHAMMER_DEFAULT_COMBO_2());
            default -> SkillManager.INSTANCE.get(SOFSkillTypes.INSTANCE.getHAMMER_DEFAULT_COMBO_3());
        };
    }
}
