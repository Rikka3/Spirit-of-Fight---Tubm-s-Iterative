package cn.solarmoon.spirit_of_fight.mixin.fighter;

import cn.solarmoon.spirit_of_fight.entity.player.IPlayerPatch;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerPatch {

}
