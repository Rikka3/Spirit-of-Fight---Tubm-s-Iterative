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

import java.util.EnumSet;
import java.util.Random;

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
    private int comboDelay = 0;
    private int targetSwingTick = -100;
    private boolean wasTargetSwinging = false;

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
        goalSelector.addGoal(1, new WarhammerPrecisionBlockGoal(this));
        goalSelector.addGoal(2, new WarhammerComboAttackGoal(this));
        goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 1.0, 32.0f));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));

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
        if (comboDelay > 0) comboDelay--;

        // Track target swing for precision blocking
        LivingEntity target = getTarget();
        if (target != null) {
            if (target.swinging && !wasTargetSwinging) {
                targetSwingTick = tickCount;
            }
            wasTargetSwinging = target.swinging;
        }

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

    public void incrementCombo() {
        setComboCount((getComboCount() + 1) % 4); // 0-3 combo cycle
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

    public int getComboDelay() {
        return comboDelay;
    }

    public void setComboDelay(int delay) {
        this.comboDelay = delay;
    }

    public int getTargetSwingTick() {
        return targetSwingTick;
    }

    /**
     * Check if target recently started swinging (within last 10 ticks)
     */
    public boolean isTargetAboutToHit() {
        return tickCount - targetSwingTick < 10;
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(1.0, 0.0, 1.0);
    }

    /**
     * AI Goal for combo attacks with proper timing
     */
    public static class WarhammerComboAttackGoal extends MeleeAttackGoal {
        private final WarhammerVindicator mob;
        private static final Random RANDOM = new Random();

        public WarhammerComboAttackGoal(WarhammerVindicator mob) {
            super(mob, 1.0, false);
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && mob.getComboDelay() <= 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                double dist = this.mob.distanceToSqr(target);
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
                
                // Check if we should attack
                if (dist <= getAttackReachSqr(target) && mob.getComboDelay() <= 0) {
                    // Add some randomness to attack timing for more natural behavior
                    if (RANDOM.nextFloat() < 0.3f) {
                        mob.setComboDelay(5 + RANDOM.nextInt(10));
                        return;
                    }
                    
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                    mob.setLastAttackTick(mob.tickCount);
                    mob.incrementCombo();
                    
                    // Add delay between combo hits for more natural flow
                    int comboDelay = 15 + RANDOM.nextInt(10); // 0.75-1.25 seconds between hits
                    mob.setComboDelay(comboDelay);
                } else {
                    super.tick();
                }
            }
        }

        private double getAttackReachSqr(LivingEntity target) {
            return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
        }

        @Override
        public void stop() {
            super.stop();
            mob.setComboCount(0);
        }
    }

    /**
     * AI Goal for precision blocking - blocks when target is about to attack
     */
    public static class WarhammerPrecisionBlockGoal extends Goal {
        private final WarhammerVindicator mob;
        private LivingEntity target;
        private int blockTime;
        private static final Random RANDOM = new Random();

        public WarhammerPrecisionBlockGoal(WarhammerVindicator mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!mob.canBlock()) return false;

            target = mob.getTarget();
            if (target == null) return false;
            double dist = mob.distanceToSqr(target);

            // Block when target is close and about to hit (detected swing)
            if (dist < 12.0 && mob.isTargetAboutToHit()) {
                return true;
            }

            // Also block reactively when target is very close and attacking
            if (dist < 6.0 && isTargetAttacking()) {
                return RANDOM.nextFloat() < 0.5f; // 50% chance to block
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return mob.isBlocking() && blockTime < 30 && target != null && mob.distanceToSqr(target) < 16.0;
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
            mob.setBlockCooldown(40 + RANDOM.nextInt(40)); // 2-4 second cooldown
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
}
