package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.util.MoveDirection;
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import cn.solarmoon.spirit_of_fight.entity.WieldStyle;
import cn.solarmoon.spirit_of_fight.entity.grab.GrabManager;
import cn.solarmoon.spirit_of_fight.poise_system.PoiseData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import cn.solarmoon.spirit_of_fight.sync.SwitchAttackCooldownPayload;
import net.neoforged.neoforge.network.PacketDistributor;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityPatch {

    @Shadow
    public abstract Level level();

    @Shadow
    public abstract int getId();

    private final Entity entity = (Entity) (Object) this;
    private MoveDirection side;
    private final GrabManager grabManager = new GrabManager((Entity) (Object) this);
    private boolean hitting = false;
    private boolean knockDown = false;
    private int chargingTime = 0;
    private WieldStyle wieldStyle = WieldStyle.DEFAULT;
    private final PoiseData poise = new PoiseData(entity, 100, 100);
    private boolean solid = false;
    private boolean guardEnabled = false;
    private boolean canUseItem = true;
    private int stunTicks = 0;
    private long lastDodgeTick = 0;
    private boolean recoveryStun = false;
    private long switchAttackCooldownUntil = 0;
    private Object activeBlockSkill = null;
    private final java.util.Map<net.minecraft.resources.ResourceLocation, Long> skillCooldowns = new java.util.HashMap<>();

    @Override
    public java.util.Map<net.minecraft.resources.ResourceLocation, Long> getSkillCooldowns() {
        return skillCooldowns;
    }

    @Override
    public long getLastDodgeTick() {
        return lastDodgeTick;
    }

    @Override
    public void setLastDodgeTick(long lastDodgeTick) {
        this.lastDodgeTick = lastDodgeTick;
    }

    @Override
    public @Nullable MoveDirection getMoveDirection() {
        return side;
    }

    @Override
    public void setMoveDirection(@Nullable MoveDirection moveDirection) {
        this.side = moveDirection;
    }

    @Override
    public @NotNull GrabManager getGrabManager() {
        return grabManager;
    }

    @Override
    public boolean isHitting() {
        return hitting;
    }

    @Override
    public void setHitting(boolean b) {
        hitting = b;
    }

    @Override
    public boolean isKnockedDown() {
        return knockDown;
    }

    @Override
    public void setKnockedDown(boolean b) {
        knockDown = b;
    }

    @Override
    public @NotNull WieldStyle getWieldStyle() {
        return wieldStyle;
    }

    @Override
    public void setWieldStyle(@NotNull WieldStyle wieldStyle) {
        this.wieldStyle = wieldStyle;
    }

    @Override
    public @NotNull PoiseData getPoise() {
        return poise;
    }

    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    private void collide(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.entity.isSolid())
            cir.setReturnValue(true);
    }

    @Override
    public boolean isSolid() {
        return solid;
    }

    @Override
    public void setSolid(boolean b) {
        solid = b;
    }

    @Override
    public boolean isGuardEnabled() {
        return guardEnabled;
    }

    @Override
    public void setGuardEnabled(boolean b) {
        guardEnabled = b;
    }

    @Override
    public boolean getCanUseItem() {
        return canUseItem;
    }

    @Override
    public void setCanUseItem(boolean b) {
        if (canUseItem && !b && entity instanceof LivingEntity living && living.isUsingItem())
            living.stopUsingItem();
        canUseItem = b;
    }

    @Override
    public int getStunTicks() {
        return stunTicks;
    }

    @Override
    public void setStunTicks(int stunTicks) {
        this.stunTicks = stunTicks;
    }

    @Override
    public boolean isRecoveryStun() {
        return recoveryStun;
    }

    @Override
    public void setRecoveryStun(boolean recoveryStun) {
        this.recoveryStun = recoveryStun;
        if (!this.level().isClientSide) {
            PacketDistributor.sendToPlayersTrackingEntity((Entity) (Object) this,
                    new cn.solarmoon.spirit_of_fight.sync.RecoveryStunPayload(this.getId(), recoveryStun));
            if ((Object) this instanceof net.minecraft.server.level.ServerPlayer player) {
                PacketDistributor.sendToPlayer(player,
                        new cn.solarmoon.spirit_of_fight.sync.RecoveryStunPayload(this.getId(), recoveryStun));
            }
        }
    }

    @Override
    public long getSwitchAttackCooldownUntil() {
        return switchAttackCooldownUntil;
    }

    @Override
    public void setSwitchAttackCooldownUntil(long switchAttackCooldownUntil) {
        this.switchAttackCooldownUntil = switchAttackCooldownUntil;
        if (!this.level().isClientSide) {
            PacketDistributor.sendToPlayersTrackingEntity((Entity) (Object) this,
                    new SwitchAttackCooldownPayload(this.getId(), switchAttackCooldownUntil));
            if ((Object) this instanceof net.minecraft.server.level.ServerPlayer player) {
                PacketDistributor.sendToPlayer(player,
                        new SwitchAttackCooldownPayload(this.getId(), switchAttackCooldownUntil));
            }
        }
    }

    @Nullable
    public Object getActiveBlockSkill() {
        return activeBlockSkill;
    }

    public void setActiveBlockSkill(@Nullable Object blockSkill) {
        this.activeBlockSkill = blockSkill;
    }

    private long comboCooldownUntil = 0;

    @Override
    public long getComboCooldownUntil() {
        return comboCooldownUntil;
    }

    @Override
    public void setComboCooldownUntil(long comboCooldownUntil) {
        this.comboCooldownUntil = comboCooldownUntil;
        if (!this.level().isClientSide) {
            PacketDistributor.sendToPlayersTrackingEntity((Entity) (Object) this,
                    new cn.solarmoon.spirit_of_fight.sync.ComboCooldownPayload(this.getId(), comboCooldownUntil));
            if ((Object) this instanceof net.minecraft.server.level.ServerPlayer player) {
                PacketDistributor.sendToPlayer(player,
                        new cn.solarmoon.spirit_of_fight.sync.ComboCooldownPayload(this.getId(), comboCooldownUntil));
            }
        }
    }
}
