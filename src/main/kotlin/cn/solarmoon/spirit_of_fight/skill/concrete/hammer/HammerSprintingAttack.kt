package cn.solarmoon.spirit_of_fight.skill.concrete.hammer

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.world.entity.LivingEntity

class HammerSprintingAttack(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("hammer:attack_sprinting") {
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
        addComponent(StuckEffectComponent(5, 0.03) { comboAnim.time in 0.3..0.9 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_SWIPE.get(), { 0.75 }) { time in 0.3..0.9 })
        addComponent(AnimPreInputAcceptComponent(1.25, entity.getPreInput(), comboAnim))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.0..0.3) entity.getForwardMoveVector(1/5f) else if (time in 0.3..0.90) entity.getForwardMoveVector(1/2.25f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(comboAnim, 0)
    }

}