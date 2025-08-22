package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.skill.SkillStartCondition
import cn.solarmoon.spark_core.skill.skillType
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.BlockSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import net.minecraft.resources.ResourceLocation

object SOFSkills {
    @JvmStatic
    fun register() {}

    fun id(name: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, name)

    val dodge = skillType(
        id("dodge"),
        listOf(),
        { DodgeSkill() },
        {}
    )

    val block = skillType(
        id("block"),
        listOf(
            SkillStartCondition("flag", "不在格挡状态") { host, level ->
                host is IEntityAnimatable<*> && host.animatable.isGuardEnabled
            }
        ),
        { BlockSkill() },
        {}
    )

}