package cn.solarmoon.spirit_of_fight.mixin.js;

import cn.solarmoon.spark_core.animation.IAnimatable;
import cn.solarmoon.spirit_of_fight.js.JSSOFAnimatable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IAnimatable.class)
public interface IAnimatableMixin extends JSSOFAnimatable {
}
