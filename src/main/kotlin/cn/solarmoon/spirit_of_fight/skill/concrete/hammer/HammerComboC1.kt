package cn.solarmoon.spirit_of_fight.skill.concrete.hammer

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox

class HammerComboC1(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val shoulderBody = createSkillAttackAnimBody("rightArm", holder, entity.level(), entity.getPatch().mainAttackSystem) {
        (firstGeom as? DBox)?.apply {
            lengths = DVector3(1.0, 1.0, 1.0)
            offsetPosition = DVector3()
        }
    }

    var targetHitCheck = false
        private set

    val comboAnim = createAnimInstance("hammer:combo_c1") {
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
        addComponent(StuckEffectComponent(5, 0.03) { comboAnim.time in 0.10..0.30 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_STAB.get(), { 0.25 },
            body = shoulderBody,
            whenTargetAttacked = { o1, o2, buffer, system, dMul ->
                val target = o2.body.owner
                targetHitCheck = true
                if (target is LivingEntity) target.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 255, false, false, false))
                entity.level().playSound(null, entity.onPos.above(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0f, 0.75f)
            }
        ) { time in 0.10..0.30 })
        addComponent(AnimPreInputAcceptComponent(0.05, entity.getPreInput(), comboAnim, limit = { targetHitCheck && it == "combo"}))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.10..0.35) entity.getForwardMoveVector(1/4f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        targetHitCheck = false
        holder.animController.setAnimation(comboAnim, 0)
    }

    override fun onEnd() {
        super.onEnd()
        entity.getPreInput().clear()
    }

}