package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.IEntityPatch;
import cn.solarmoon.spirit_of_fight.entity.player.IPlayerPatch;
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerPatch {

    private SkillTreeSet treeSet;

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void preventAttackWhenStunned(CallbackInfo ci) {
        if (this instanceof IEntityPatch patch && (patch.getStunTicks() > 0 || patch.isKnockedDown())) {
            ci.cancel();
        }
    }

    @Override
    public @Nullable SkillTreeSet getCurrentSkillSet() {
        return treeSet;
    }

    @Override
    public void setCurrentSkillSet(@Nullable SkillTreeSet skillTrees) {
        treeSet = skillTrees;
    }

}
