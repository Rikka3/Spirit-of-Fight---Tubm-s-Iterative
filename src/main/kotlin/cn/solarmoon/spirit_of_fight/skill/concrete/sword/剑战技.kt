package cn.solarmoon.spirit_of_fight.skill.concrete.sword

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.IHoldReleaseSkill
import cn.solarmoon.spirit_of_fight.skill.component.*
import net.minecraft.world.entity.LivingEntity
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3d

class 剑战技(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder), IHoldReleaseSkill {

    override var releaseCheck = false

    val guardAnim = createAnimInstance("sword:skill_keeping") {
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

    val hitAnim = createAnimInstance("sword:skill_hit") {
        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(AnimGuardComponent(entity, guardAnim,
            onSuccessGuard = { _, event -> event.isCanceled = true }
        ))
        addComponent(AnimBoxAttackComponent(entity, hitAnim, SOFHitTypes.HEAVY_UPSTROKE.get(), { 1.2 },
            fightSpiritModifier = null
        ) { time in 0.25..0.55 })
        addComponent(StuckEffectComponent(5, 0.05) { hitAnim.time in 0.25..0.55 })
        addComponent(AnimPreInputAcceptComponent(0.0, entity.getPreInput(), guardAnim, limit = { it == "special_attack" }))
        addComponent(AnimMoveSetComponent(entity, hitAnim) { if (time in 0.25..0.5) entity.getForwardMoveVector(1/6f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(guardAnim, 3)
    }

    override fun onEnd() {
        super.onEnd()
        releaseCheck = false
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
    }

}