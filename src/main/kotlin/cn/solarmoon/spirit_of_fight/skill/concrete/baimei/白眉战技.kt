package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.IHoldReleaseSkill
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3d

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
            if (time > 0.4 && releaseCheck) {
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
        addComponent(StuckEffectComponent(3, 0.1) { keepingAnim.time in 0.10..0.40 })
        addComponent(StuckEffectComponent(5, 0.05) { hitAnim.time in 0.05..0.15 })

        addComponent(AnimMoveSetComponent(entity, keepingAnim) { if (time in 0.15..0.40) entity.getForwardMoveVector(1/6f).toVector3d().apply {x=-x;z=-z}.toVec3() else null })
        addComponent(AnimBoxAttackComponent(entity, keepingAnim, SOFHitTypes.HEAVY_CHOP.get(), { 0.6 },
            body = entity.getPatch().getOffAttackBody(),
            fightSpiritModifier = null
        ) { time in 0.1..0.4 })
        addComponent(AnimBoxAttackComponent(entity, hitAnim, SOFHitTypes.KNOCKDOWN_SWIPE.get(), { 1.2 },
                soundEvent = SoundEvents.PLAYER_ATTACK_CRIT,
            fightSpiritModifier = null
        ) { time in 0.05..0.15 })
        addComponent(AnimMoveSetComponent(entity, hitAnim) { if (time in 0.10..0.40) entity.getForwardMoveVector(1/5f) else null })
        addComponent(AnimPreInputAcceptComponent(0.0, entity.getPreInput(), keepingAnim, limit = { it == "special_attack" }))
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(keepingAnim, 0)
    }

    override fun onEnd() {
        super.onEnd()
        releaseCheck = false
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
    }

}