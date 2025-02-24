package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.util.MoveDirection;
import cn.solarmoon.spirit_of_fight.fighter.IEntityPatch;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin implements IEntityPatch {

    private MoveDirection side;

    @Override
    public @Nullable MoveDirection getMoveDirection() {
        return side;
    }

    @Override
    public void setMoveDirection(@Nullable MoveDirection moveDirection) {
        this.side = moveDirection;
    }

}
