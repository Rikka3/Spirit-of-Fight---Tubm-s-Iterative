package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spark_core.util.MoveDirection;
import cn.solarmoon.spirit_of_fight.fighter.player.IPlayerPatch;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerPatch {

}
