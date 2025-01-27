package cn.solarmoon.spirit_of_fight.skill.concrete.mace

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
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity

class MaceCombo0(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("mace:combo_0") {
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
        // 卡肉 第一个参数持续时间/tick 第二个改变动画的速度为多少 第三个 是否启用
        addComponent(StuckEffectComponent(4, 0.05) { comboAnim.time in 0.25..0.5 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.LIGHT_CHOP.get(), { 0.85 }, soundEvent = SoundEvents.PLAYER_ATTACK_STRONG) { time in 0.25..0.5 })
        addComponent(AnimPreInputAcceptComponent(0.65, entity.getPreInput(), comboAnim))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.20..0.3) entity.getForwardMoveVector(1/6f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(comboAnim, 0)
    }

}