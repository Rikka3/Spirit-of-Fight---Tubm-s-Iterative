package cn.solarmoon.spirit_of_fight.skill.concrete.hammer

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.entity.getForwardMoveVector
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.phys.thread.getPhysLevel
import cn.solarmoon.spark_core.phys.toDQuaternion
import cn.solarmoon.spark_core.phys.toDVector3
import cn.solarmoon.spark_core.phys.toRadians
import cn.solarmoon.spark_core.registry.common.SparkBodyTypes
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.body.createEmptyBody
import cn.solarmoon.spirit_of_fight.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFBodyTypes
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import org.joml.Quaterniond
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox
import org.ode4j.ode.OdeHelper

class HammerCombo2(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val aoe = OdeHelper.createBody(SOFBodyTypes.ATTACK.get(), holder, "hammer_aoe", false, holder.animatable.level().getPhysLevel().world).apply {
        disable()
        val geom = OdeHelper.laterCreateBox(this, holder.animatable.level().getPhysLevel().world, DVector3(2.0, 2.0, 2.0))

        onPhysTick {
            val pos = entity.position()
            val distance = 2.0
            val forward = Vec3.directionFromRotation(0f, holder.animatable.yRot)
            val target = pos.add(forward.scale(distance))
            geom.position = target.toDVector3()
            geom.quaternion = Quaterniond().rotateY(holder.animatable.yRot.toDouble().toRadians()).toDQuaternion()
            if (holder.animatable.level().isClientSide) SparkVisualEffects.GEOM.getRenderableBox(geom.uuid.toString()).refresh(geom)
        }

        geom.onCollide { o2, buffer ->
            holder.animatable.getPatch().mainAttackSystem.commonGeomAttack(geom, o2)
        }
    }

    val comboAnim = createAnimInstance("hammer:combo_2") {
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
        addComponent(StuckEffectComponent(5, 0.03) { comboAnim.time in 0.45..0.75 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.KNOCKDOWN_CHOP.get(), { 1.5 }, soundEvent = SoundEvents.PLAYER_ATTACK_KNOCKBACK) { time in 0.45..0.75 })
        addComponent(AnimMoveSetComponent(entity, comboAnim) { if (time in 0.0..0.65) entity.getForwardMoveVector(1/5f) else null })
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