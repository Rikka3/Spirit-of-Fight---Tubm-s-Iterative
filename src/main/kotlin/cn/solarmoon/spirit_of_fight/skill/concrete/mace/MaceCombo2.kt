package cn.solarmoon.spirit_of_fight.skill.concrete.mace

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3d

class MaceCombo2(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val largeBody = createSkillAttackAnimBody("rightItem", holder, entity.level(), entity.getPatch().mainAttackSystem) {
        onPhysTick {
            (firstGeom as? DBox)?.let {
                it.lengths = DVector3(1.5, 1.5, 1.5)
                it.offsetPosition = DVector3(0.0, 0.0, -0.65)
            }
        }
    }

    val comboAnim = createAnimInstance("mace:combo_2") {
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
        addComponent(StuckEffectComponent(6, 0.05) { comboAnim.time in 0.65..0.90 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_UPSTROKE.get(), { 1.2 }, soundEvent = SoundEvents.PLAYER_ATTACK_KNOCKBACK, body = largeBody) { time in 0.65..0.9 })
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.70..0.95) entity.getForwardMoveVector(1/6f).toVector3d().apply {x=x; y = y+0.12 ;z=z}.toVec3() else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(comboAnim, 0)
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
    }

    override fun onEnd() {
        super.onEnd()
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
    }

}