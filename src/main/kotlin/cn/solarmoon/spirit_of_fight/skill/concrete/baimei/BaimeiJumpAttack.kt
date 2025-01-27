package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3d

class BaimeiJumpAttack(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val legBody = createSkillAttackAnimBody("rightLeg", holder, entity.level(), entity.getPatch().mainAttackSystem) {
        disable()
        (firstGeom as? DBox)?.let {
            it.offsetPosition = DVector3(0.0, -0.5, 0.0)
            it.lengths = DVector3(1.0, 1.0,1.0)
        }
    }

    val comboAnim = createAnimInstance("baimei:attack_jump") {
        shouldTurnBody = true
        onEnable {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(StuckEffectComponent(3, 0.05) { comboAnim.time in 0.0..0.30 })
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.0..0.20) entity.getForwardMoveVector(1/3f).toVector3d().apply {x=x;y=y-0.20;z=z}.toVec3() else null })
        addComponent(AnimPreInputAcceptComponent(0.50, entity.getPreInput(), comboAnim))
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_STAB.get(), { 0.5 }, body = legBody,soundEvent = SoundEvents.PLAYER_ATTACK_KNOCKBACK) { time in 0.0..0.3 })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(comboAnim, 0)
    }

}