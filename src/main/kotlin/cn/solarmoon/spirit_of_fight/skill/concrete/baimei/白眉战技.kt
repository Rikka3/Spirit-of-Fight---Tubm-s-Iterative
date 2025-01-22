package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.feature.fight_skill.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.IHoldReleaseSkill
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimGuardComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.world.entity.LivingEntity

class 白眉战技(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder), IHoldReleaseSkill {

    override var releaseCheck = false

    val keepingAnim = createAnimInstance("baimei:skill_keeping") {
        shouldTurnBody = true
        rejectNewAnim = { name != hitAnim.name }
        onEnable {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onTick {
            if (releaseCheck) {
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

    val hitAnim = createAnimInstance("baimei:skill_hit") {
        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(AnimBoxAttackComponent(entity, keepingAnim, SOFHitTypes.HEAVY_CHOP.get(), { 2.0 },
            body = entity.getPatch().getOffAttackBody(),
            fightSpiritModifier = null
        ) { time in 0.1..0.4 })
        addComponent(AnimBoxAttackComponent(entity, hitAnim, SOFHitTypes.HEAVY_SWIPE.get(), { 2.0 },
            fightSpiritModifier = null
        ) { time in 0.15..0.4 })
        addComponent(AnimPreInputAcceptComponent(0.0, entity.getPreInput(), keepingAnim, limit = { it == "special_attack" }))
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(keepingAnim, 5)
    }

    override fun onEnd() {
        super.onEnd()
        releaseCheck = false
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
    }

}