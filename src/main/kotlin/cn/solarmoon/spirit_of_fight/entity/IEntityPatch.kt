package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.entity.grab.GrabManager
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTreeSet
import net.minecraft.world.entity.Entity

interface IEntityPatch {

    var moveDirection: MoveDirection?

    var currentSkillSet: SkillTreeSet?

    val grabManager: GrabManager

    var hitting: Boolean

}