package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.util.MoveDirection;
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import cn.solarmoon.spirit_of_fight.entity.WieldStyle;
import cn.solarmoon.spirit_of_fight.entity.grab.GrabManager;
import cn.solarmoon.spirit_of_fight.poise_system.PoiseData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements IEntityPatch {

    private final Entity entity = (Entity) (Object) this;
    private MoveDirection side;
    private final GrabManager grabManager = new GrabManager((Entity) (Object) this);
    private boolean hitting = false;
    private boolean knockDown = false;
    private int chargingTime = 0;
    private WieldStyle wieldStyle = WieldStyle.DEFAULT;
    private final PoiseData poise = new PoiseData(entity, 100, 100);
    private boolean solid = false;
    private boolean guardEnabled = true;

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
        if (this.entity.isSolid()) cir.setReturnValue(true);
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
}
