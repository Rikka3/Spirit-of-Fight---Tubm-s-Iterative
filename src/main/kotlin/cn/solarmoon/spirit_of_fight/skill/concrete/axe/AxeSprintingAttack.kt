package cn.solarmoon.spirit_of_fight.skill.concrete.axe

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.IHoldReleaseSkill
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.world.entity.LivingEntity

class AxeSprintingAttack(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder), IHoldReleaseSkill {

    override var releaseCheck: Boolean = false

    val comboAnim = createAnimInstance("axe:attack_sprinting") {
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
        addComponent(StuckEffectComponent(4, 0.05) { comboAnim.time in 0.25..0.5 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_SWIPE.get(), { 0.65 }) { time in 0.25..0.5 })
        addComponent(AnimPreInputAcceptComponent(0.75, entity.getPreInput(), comboAnim))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.0..0.15) entity.getForwardMoveVector(1/4f) else if (time in 0.15..0.65) entity.getForwardMoveVector(1/5f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        releaseCheck = false
        holder.animController.setAnimation(comboAnim, 0)
    }

}