package cn.solarmoon.spirit_of_fight.fighter.player

import cn.solarmoon.spark_core.animation.vanilla.asAnimatable
import cn.solarmoon.spark_core.phys.createAnimatedCubeBody
import cn.solarmoon.spark_core.skill.controller.getAllSkillControllers
import cn.solarmoon.spirit_of_fight.body.createGuardAnimBody
import cn.solarmoon.spirit_of_fight.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.EntityPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFBodyTypes
import cn.solarmoon.spirit_of_fight.skill.controller.AxeFightSkillController
import cn.solarmoon.spirit_of_fight.skill.controller.BaimeiFightSkillController
import cn.solarmoon.spirit_of_fight.skill.controller.BowFightSkillController
import cn.solarmoon.spirit_of_fight.skill.controller.HammerFightSkillController
import cn.solarmoon.spirit_of_fight.skill.controller.MaceFightSkillController
import cn.solarmoon.spirit_of_fight.skill.controller.SwordFightSkillController
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import org.ode4j.ode.DBody


class PlayerPatch(
    val player: Player
): EntityPatch(player) {

    val level = player.level()
    val animatable = player.asAnimatable()
    val attackBodyRight = lazy { createSkillAttackAnimBody("rightItem", animatable, level, mainAttackSystem) }
    val attackBodyLeft = lazy { createSkillAttackAnimBody("leftItem", animatable, level, offAttackSystem) }
    val guardBodyRight = lazy { createGuardAnimBody("rightItem", animatable, level) }
    val guardBodyLeft = lazy { createGuardAnimBody("leftItem", animatable, level) }

    override fun onJoinLevel(level: Level) {
        super.onJoinLevel(level)

        getMainAttackBody()
        getOffAttackBody()
        getMainGuardBody()
        getOffGuardBody()

        animatable.model.bones.values.filter { it.name !in listOf("rightItem", "leftItem") }.forEach { bone ->
            createAnimatedCubeBody(bone.name, SOFBodyTypes.PLAYER_BODY.get(), animatable, level)
        }

        player.getAllSkillControllers().add(SwordFightSkillController(player, player.asAnimatable()))
        player.getAllSkillControllers().add(AxeFightSkillController(player, player.asAnimatable()))
        player.getAllSkillControllers().add(HammerFightSkillController(player, player.asAnimatable()))
        player.getAllSkillControllers().add(MaceFightSkillController(player, player.asAnimatable()))
        player.getAllSkillControllers().add(BaimeiFightSkillController(player, player.asAnimatable()))
        player.getAllSkillControllers().add(BowFightSkillController(player, player.asAnimatable()))
    }

    override fun getMainAttackBody(): DBody? {
        return attackBodyRight.value
    }

    override fun getOffAttackBody(): DBody? {
        return attackBodyLeft.value
    }

    override fun getMainGuardBody(): DBody? {
        return guardBodyRight.value
    }

    override fun getOffGuardBody(): DBody? {
        return guardBodyLeft.value
    }

}