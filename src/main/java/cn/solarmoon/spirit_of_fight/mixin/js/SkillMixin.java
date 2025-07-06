package cn.solarmoon.spirit_of_fight.mixin.js;

import cn.solarmoon.spark_core.skill.Skill;
import cn.solarmoon.spirit_of_fight.js.JSSOFSkill;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Skill.class)
public class SkillMixin implements JSSOFSkill {
}
