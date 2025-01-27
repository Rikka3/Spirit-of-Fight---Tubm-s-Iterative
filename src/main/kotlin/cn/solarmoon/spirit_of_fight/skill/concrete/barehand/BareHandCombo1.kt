package cn.solarmoon.spirit_of_fight.skill.concrete.barehand

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spark_core.skill.controller.getTypedSkillController
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import cn.solarmoon.spirit_of_fight.skill.controller.FightSkillController
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity

class BareHandCombo1(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val comboAnim = createAnimInstance("barehand:combo_1") {
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
        addComponent(StuckEffectComponent(2, 0.05) { comboAnim.time in 0.25..0.5 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.LIGHT_STAB.get(), { 0.5 },
                soundEvent = SoundEvents.PLAYER_ATTACK_WEAK,
            body = entity.getPatch().getOffAttackBody(),
            baseAttackSpeed = null
        ) { time in 0.25..0.5 })
        addComponent(AnimPreInputAcceptComponent(0.55, entity.getPreInput(), comboAnim))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.20..0.3) entity.getForwardMoveVector(1/5f) else null })
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