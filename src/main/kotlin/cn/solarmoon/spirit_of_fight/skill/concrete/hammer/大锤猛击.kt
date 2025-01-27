package cn.solarmoon.spirit_of_fight.skill.concrete.hammer

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity

class 大锤猛击(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("hammer:skill") {
        shouldTurnBody = true
        onEnable {
            entity.getFightSpirit().clear()
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.LIGHT_CHOP.get()) { time in 0.75..0.95 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.KNOCKDOWN_UPSTROKE.get(), soundEvent = SoundEvents.PLAYER_ATTACK_STRONG,
            fightSpiritModifier = null
        ) { time in 0.75..0.95 })
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.65..0.90) entity.getForwardMoveVector(1/4f) else null })
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