package cn.solarmoon.spirit_of_fight.skill.concrete.sword

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getAttackAnimSpeed
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.world.entity.LivingEntity

class SwordCombo0(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("sword:combo_0") {
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
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.LIGHT_CHOP.get()) { time in 0.25..0.5 })
        addComponent(AnimPreInputAcceptComponent(0.5, entity.getPreInput(), comboAnim))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.20..0.3) entity.getForwardMoveVector(1/8f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(comboAnim, 0)
    }

}