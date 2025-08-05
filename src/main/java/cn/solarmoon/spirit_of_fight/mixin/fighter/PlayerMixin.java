package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.player.IPlayerPatch;
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerPatch {

    private SkillTreeSet treeSet;

    @Override
    public @Nullable SkillTreeSet getCurrentSkillSet() {
        return treeSet;
    }

    @Override
    public void setCurrentSkillSet(@Nullable SkillTreeSet skillTrees) {
        treeSet = skillTrees;
    }

}
