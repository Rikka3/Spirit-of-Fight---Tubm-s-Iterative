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
import cn.solarmoon.spark_core.phys.toVec3
import cn.solarmoon.spark_core.registry.common.SparkBodyTypes
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.body.createEmptyBody
import cn.solarmoon.spirit_of_fight.body.createForwardAttackBox
import cn.solarmoon.spirit_of_fight.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFBodyTypes
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import org.joml.Quaterniond
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox
import org.ode4j.ode.OdeHelper
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

class HammerCombo2(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val aoe = createForwardAttackBox(entity, 2.0, entity.getPatch().mainAttackSystem)
    var doParticle = false

    val comboAnim = createAnimInstance("hammer:combo_2") {
        shouldTurnBody = true
        onEnable {
            doParticle = false
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onTick {
            val pos = aoe.firstGeom.position.toVec3().subtract(0.0, 1.0, 0.0)

            val particleCount = 20
            val speed = 0.5

            if (time >= 0.55 && !doParticle) {
                doParticle = true
                SparkVisualEffects.CAMERA_SHAKE.shake(10, 1.5f, 3f)
                for (i in 0 until particleCount) {
                    val angle = 2 * Math.PI * i / particleCount

                    val velocityX = speed * cos(angle)
                    val velocityZ = speed * sin(angle)

                    entity.level().addParticle(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z,
                        velocityX,
                        0.05,
                        velocityZ
                    )
                }
            }
        }

        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(StuckEffectComponent(5, 0.03) { comboAnim.time in 0.45..0.75 })
        addComponent(AnimBoxAttackComponent(entity, comboAnim, SOFHitTypes.KNOCKDOWN_CHOP.get(), { 1.5 }, body = aoe, soundEvent = SoundEvents.PLAYER_ATTACK_KNOCKBACK) { time in 0.45..0.75 })
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