package cn.solarmoon.spirit_of_fight.entity;

import cn.solarmoon.spirit_of_fight.registry.common.SOFItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

/**
 * A specialized vindicator that wields an iron warhammer and uses the mod's combat system.
 * Spawns during raids and in woodland mansions.
 * 
 * This entity relies on the mixin system for combat integration (IEntityPatch, IEntityAnimatable).
 * The mixin system provides these implementations at runtime through EntityMixin.
 */
public class WarhammerVindicator extends Vindicator {

    private static final EntityDataAccessor<Integer> COMBO_COUNT = SynchedEntityData.defineId(WarhammerVindicator.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BLOCKING = SynchedEntityData.defineId(WarhammerVindicator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FIGHT_SPIRIT = SynchedEntityData.defineId(WarhammerVindicator.class, EntityDataSerializers.INT);

    public static AttributeSupplier.Builder createAttributes() {
        return Vindicator.createAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0) // Higher damage for warhammer
                .add(Attributes.MAX_HEALTH, 30.0) // Slightly more health than regular vindicator
                .add(Attributes.MOVEMENT_SPEED, 0.28); // Slightly slower due to heavy weapon
    }

    private long lastAttackTick = 0;
    private int blockCooldown = 0;
    private int specialAttackCooldown = 0;

    public WarhammerVindicator(EntityType<? extends WarhammerVindicator> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COMBO_COUNT, 0);
        builder.define(IS_BLOCKING, false);
        builder.define(FIGHT_SPIRIT, 0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new WarhammerBlockGoal(this));
        goalSelector.addGoal(2, new WarhammerGroundSlamGoal(this));
        goalSelector.addGoal(3, new WarhammerSprintAttackGoal(this));
        goalSelector.addGoal(4, new WarhammerComboAttackGoal(this));
        goalSelector.addGoal(5, new MoveTowardsTargetGoal(this, 1.0, 32.0f));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(0, new HurtByTargetGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData);
        setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SOFItems.getIRON_WARHAMMER().get()));
        setDropChance(EquipmentSlot.MAINHAND, 0.05f);
        return data;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ComboCount", getComboCount());
        tag.putInt("FightSpirit", getFightSpirit());
        tag.putBoolean("IsBlocking", isBlocking());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setComboCount(tag.getInt("ComboCount"));
        setFightSpirit(tag.getInt("FightSpirit"));
        setBlocking(tag.getBoolean("IsBlocking"));
    }

    @Override
    public void tick() {
        super.tick();

        // Decrease cooldowns
        if (blockCooldown > 0) blockCooldown--;
        if (specialAttackCooldown > 0) specialAttackCooldown--;

        // Reset combo after timeout (3 seconds)
        if (tickCount - lastAttackTick > 60 && getComboCount() > 0) {
            setComboCount(0);
        }
    }

    public int getComboCount() {
        return entityData.get(COMBO_COUNT);
    }

    public void setComboCount(int value) {
        entityData.set(COMBO_COUNT, value);
    }

    public boolean isBlocking() {
        return entityData.get(IS_BLOCKING);
    }

    public void setBlocking(boolean value) {
        entityData.set(IS_BLOCKING, value);
    }

    public int getFightSpirit() {
        return entityData.get(FIGHT_SPIRIT);
    }

    public void setFightSpirit(int value) {
        entityData.set(FIGHT_SPIRIT, Math.max(0, Math.min(200, value)));
    }

    public void addFightSpirit(int amount) {
        setFightSpirit(getFightSpirit() + amount);
    }

    public boolean canUseSpecialAttack() {
        return getFightSpirit() >= 100 && specialAttackCooldown <= 0;
    }

    public boolean canBlock() {
        IEntityPatch patch = (IEntityPatch) this;
        boolean isStunned = patch.getStunTicks() > 0;
        return blockCooldown <= 0 && !isBlocking() && !isStunned;
    }

    public void setBlockCooldown(int cooldown) {
        this.blockCooldown = cooldown;
    }

    public int getBlockCooldown() {
        return blockCooldown;
    }

    public void setSpecialAttackCooldown(int cooldown) {
        this.specialAttackCooldown = cooldown;
    }

    public int getSpecialAttackCooldown() {
        return specialAttackCooldown;
    }

    public void setLastAttackTick(long tick) {
        this.lastAttackTick = tick;
    }

    public long getLastAttackTick() {
        return lastAttackTick;
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(1.0, 0.0, 1.0);
    }

    /**
     * AI Goal for combo attacks
     */
    public static class WarhammerComboAttackGoal extends MeleeAttackGoal {
        private final WarhammerVindicator mob;

        public WarhammerComboAttackGoal(WarhammerVindicator mob) {
            super(mob, 1.0, false);
            this.mob = mob;
        }

        @Override
        public void stop() {
            super.stop();
            mob.setComboCount(0);
        }
    }

    /**
     * AI Goal for blocking
     */
    public static class WarhammerBlockGoal extends Goal {
        private final WarhammerVindicator mob;
        private LivingEntity target;
        private int blockTime;

        public WarhammerBlockGoal(WarhammerVindicator mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!mob.canBlock()) return false;

            target = mob.getTarget();
            if (target == null) return false;
            double dist = mob.distanceToSqr(target);

            // Block when target is close and attacking
            return dist < 9.0 && isTargetAttacking();
        }

        @Override
        public boolean canContinueToUse() {
            return mob.isBlocking() && blockTime < 40 && target != null && mob.distanceToSqr(target) < 16.0;
        }

        @Override
        public void start() {
            mob.setBlocking(true);
            ((IEntityPatch) mob).setGuardEnabled(true);
            blockTime = 0;
        }

        @Override
        public void stop() {
            mob.setBlocking(false);
            ((IEntityPatch) mob).setGuardEnabled(false);
            mob.setBlockCooldown(60); // 3 second cooldown
        }

        @Override
        public void tick() {
            blockTime++;
            if (target != null) {
                mob.getLookControl().setLookAt(target);
            }
        }

        private boolean isTargetAttacking() {
            if (target == null) return false;
            return target.isUsingItem() || target.swinging;
        }
    }

    /**
     * AI Goal for ground slam special attack
     */
    public static class WarhammerGroundSlamGoal extends Goal {
        private final WarhammerVindicator mob;

        public WarhammerGroundSlamGoal(WarhammerVindicator mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!mob.canUseSpecialAttack()) return false;

            LivingEntity target = mob.getTarget();
            if (target == null) return false;
            return mob.distanceToSqr(target) < 16.0; // Within 4 blocks
        }

        @Override
        public void start() {
            LivingEntity target = mob.getTarget();
            if (target == null) return;

            // Look at target
            mob.getLookControl().setLookAt(target);

            // Deal AOE damage in radius
            Level level = mob.level();
            AABB aabb = new AABB(mob.position(), mob.position()).inflate(4.0, 1.0, 4.0);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb, e -> e != mob);

            for (LivingEntity entity : entities) {
                if (entity instanceof Player || entity instanceof AbstractVillager || entity instanceof IronGolem) {
                    mob.doHurtTarget(entity);
                }
            }

            // Knockback nearby entities
            for (LivingEntity entity : entities) {
                double dx = entity.getX() - mob.getX();
                double dz = entity.getZ() - mob.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > 0) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(
                            (dx / dist) * 0.8,
                            0.3,
                            (dz / dist) * 0.8
                    ));
                }
            }

            mob.setFightSpirit(mob.getFightSpirit() - 100);
            mob.setSpecialAttackCooldown(200); // 10 second cooldown
        }

        @Override
        public boolean canContinueToUse() {
            return false; // One-shot goal
        }
    }

    /**
     * AI Goal for sprint attack
     */
    public static class WarhammerSprintAttackGoal extends Goal {
        private final WarhammerVindicator mob;
        private LivingEntity target;
        private boolean sprinting;

        public WarhammerSprintAttackGoal(WarhammerVindicator mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = mob.getTarget();
            if (target == null) return false;
            double dist = mob.distanceToSqr(target);

            // Sprint when target is 4-8 blocks away
            return dist >= 16.0 && dist <= 64.0 && mob.onGround() && !mob.isBlocking();
        }

        @Override
        public void start() {
            target = mob.getTarget();
            sprinting = false;
        }

        @Override
        public void tick() {
            if (target == null) return;
            double dist = mob.distanceToSqr(target);

            if (dist > 16.0) {
                mob.getNavigation().moveTo(target, 1.5);
                mob.setSprinting(true);
                sprinting = true;
            } else if (sprinting && dist <= 16.0) {
                // Perform sprint attack with bonus damage
                mob.doHurtTarget(target);
                sprinting = false;
            }
        }

        @Override
        public void stop() {
            mob.setSprinting(false);
            sprinting = false;
        }

        @Override
        public boolean canContinueToUse() {
            if (target == null) return false;
            return mob.distanceToSqr(target) > 9.0 && !mob.swinging;
        }
    }
}
