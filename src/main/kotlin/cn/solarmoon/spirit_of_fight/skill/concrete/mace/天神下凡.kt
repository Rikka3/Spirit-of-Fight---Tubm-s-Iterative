package cn.solarmoon.spirit_of_fight.skill.concrete.mace

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.isFalling
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

class 天神下凡(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val fallAttackAnim = createAnimInstance("mace:attack_jump_special") {
        shouldTurnBody = true

        onPhysTick {
            if (time >= 1.0) {
                if (entity.isFalling()) {
                    time = 1.0
                } else {
                    end()
                }
            }
        }

        onTick {
            if (!entity.isFalling()) entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(AnimBoxAttackComponent(entity, fallAttackAnim, SOFHitTypes.KNOCKDOWN_CHOP.get(), { 1.0 }) { time in 0.65..1.3 })
        addComponent(AnimMoveSetComponent(entity, fallAttackAnim) {
            if (time in 0.0..0.2) Vec3(0.0, 1.0, 0.0) else if (time in 0.2..0.6) Vec3.ZERO else null
        })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(fallAttackAnim, 0)
    }

}