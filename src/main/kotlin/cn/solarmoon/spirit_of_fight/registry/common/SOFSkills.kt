package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spark_core.skill.skillType
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.GuardSkill
import net.minecraft.resources.ResourceLocation

object SOFSkills {
    @JvmStatic
    fun register() {}

    fun id(name: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, name)

    val dodge = skillType(id("dodge"), { DodgeSkill() }, {})

    val guard = skillType(id("guard"), { GuardSkill() }, {})

}