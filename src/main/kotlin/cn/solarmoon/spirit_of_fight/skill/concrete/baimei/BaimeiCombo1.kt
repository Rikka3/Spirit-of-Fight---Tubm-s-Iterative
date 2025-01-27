package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.world.entity.LivingEntity

class BaimeiCombo1(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("baimei:combo_1") {
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
        addComponent(StuckEffectComponent(2, 0.1) { comboAnim.time in 0.15..0.30 })
        addComponent(StuckEffectComponent(3, 0.1) { comboAnim.time in 0.35..0.45 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.LIGHT_UPSTROKE.get(), { 0.7 }) { time in 0.15..0.30 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.LIGHT_CHOP.get(), { 0.5 }, body = entity.getPatch().getOffAttackBody()) { time in 0.35..0.45 })
        addComponent(AnimPreInputAcceptComponent(0.45, entity.getPreInput(), comboAnim))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.05..0.20) entity.getForwardMoveVector(1/6f) else if (time in 0.35..0.50) entity.getForwardMoveVector(1/3f) else null })
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