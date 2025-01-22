package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.feature.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox

class ShieldComboC1(
    holder: IEntityAnimatable<out LivingEntity>
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val shieldBody = createSkillAttackAnimBody("leftItem", holder, entity.level(), entity.getPatch().offAttackSystem) {
        (firstGeom as? DBox)?.let {
            it.lengths = DVector3(0.5, 1.0, 1.55)
            it.offsetPosition = DVector3(-0.25, 0.0, -0.25)
        }
    }

    var targetHitCheck = false
        private set

    val comboAnim = createAnimInstance("shield:combo_c1") {
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
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.HEAVY_STAB.get(), { 0.5 },
            body = shieldBody,
            whenTargetAttacked = { o1, o2, buffer, system, dMul ->
                val target = o2.body.owner
                targetHitCheck = true
                if (target is LivingEntity) target.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 255, false, false, false))
                entity.level().playSound(null, entity.onPos.above(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0f, 0.75f)
            }
        ) { time in 0.3..0.5 })
        addComponent(AnimPreInputAcceptComponent(0.0, entity.getPreInput(), comboAnim, limit = { targetHitCheck && it != "special_attack" }))
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.2..0.5) entity.getForwardMoveVector(1/8f) else null })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(comboAnim, 0)
    }

    override fun onEnd() {
        super.onEnd()
        entity.getPreInput().clear()
        targetHitCheck = false
    }

}