package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity

class BaimeiCombo2(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("baimei:combo_2") {
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
        addComponent(StuckEffectComponent(5, 0.05) { comboAnim.time in 0.50..0.70 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_CHOP.get(), { 1.2 }, body = entity.getPatch().getOffAttackBody(),
                soundEvent = SoundEvents.PLAYER_ATTACK_CRIT
        ) { time in 0.50..0.70 })
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.15..0.55) entity.getForwardMoveVector(1/4f) else null })
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