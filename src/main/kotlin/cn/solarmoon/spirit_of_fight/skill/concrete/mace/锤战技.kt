package cn.solarmoon.spirit_of_fight.skill.concrete.mace

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.IHoldReleaseSkill
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.world.entity.LivingEntity

class 锤战技(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder), IHoldReleaseSkill {

    override var releaseCheck = false
    var keepTick = 0

    val keepAnim = createAnimInstance("mace:skill_keeping") {
        shouldTurnBody = true
        rejectNewAnim = { name != hitAnim.name }
        onEnable {
            keepTick = 0
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onTick {
            keepTick++
            if (time > 0.55 && releaseCheck) {
                cancel()
            }
        }

        onEnd {
            if (entity.getFightSpirit().isFull) {
                entity.getFightSpirit().clear()
                holder.animController.setAnimation(hitAnim, 0)
            } else end()
        }
    }

    val hitAnim = createAnimInstance("mace:skill_hit") {
        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(AnimBoxAttackComponent(entity, hitAnim, SOFHitTypes.KNOCKDOWN_UPSTROKE.get(), { 2.0 },
            fightSpiritModifier = null
        ) { time in 0.1..0.7 })
        addComponent(AnimPreInputAcceptComponent(0.0, entity.getPreInput(), keepAnim, limit = { it == "special_attack" }))
        addComponent(AnimMoveSetComponent(entity, hitAnim) { if (time in 0.05..0.95) entity.getForwardMoveVector(0.5f + keepTick / 40f - hitAnim.getProgress().toFloat()) else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(keepAnim, 5)
    }

    override fun onEnd() {
        super.onEnd()
        releaseCheck = false
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
    }

}