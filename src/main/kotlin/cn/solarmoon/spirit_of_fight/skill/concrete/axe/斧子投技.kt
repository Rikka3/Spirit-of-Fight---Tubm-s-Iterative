package cn.solarmoon.spirit_of_fight.skill.concrete.axe

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.phys.toRadians
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.registry.common.SOFHitTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.div
import kotlin.math.cos
import kotlin.math.sin

class 斧子投技(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    var grab: LivingEntity? = null

    val startAnim = createAnimInstance("axe:skill") {
        shouldTurnBody = true

        onSwitch {
            if (it?.name !in listOf(pullAnim.name, hitAnim.name)) end()
        }
    }

    val pullAnim = createAnimInstance("axe:skill_pull") {
        shouldTurnBody = true
        onTick {
            grab?.let { grab ->
                if (time in 0.3..0.5) {
                    grab.deltaMovement = entity.position().subtract(grab.position()).normalize().div(2.5)
                }
            }
        }

        onEnable {
            grab?.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
            grab?.putFlag(SparkFlags.SILENCE, true)
            grab?.putFlag(SparkFlags.DISARM, true)
            grab?.putFlag(SparkFlags.DISABLE_PRE_INPUT, true)
        }

        onEnd {
            end()
        }
    }

    val hitAnim = createAnimInstance("axe:skill_hit") {
        shouldTurnBody = true
        onEnable {
            entity.getFightSpirit().clear()
            grab?.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
            grab?.putFlag(SparkFlags.SILENCE, true)
            grab?.putFlag(SparkFlags.DISARM, true)
            grab?.putFlag(SparkFlags.DISABLE_PRE_INPUT, true)
            grab?.setPos(entity.position().add(-sin(entity.yRot.toRadians()).toDouble(), 0.0, cos(entity.yRot.toRadians()).toDouble()))
        }

        onTick {
            // 保证生物不动
            grab!!.deltaMovement = Vec3.ZERO

            // 玩家强击退霸体
            entity.deltaMovement = Vec3(0.0, entity.deltaMovement.y, 0.0)
        }

        onEnd {
            end()
        }
    }

    init {
        addComponent(StuckEffectComponent(2, 0.05) { startAnim.time in 0.4..0.60 })
        addComponent(
            AnimBoxAttackComponent(entity, startAnim, SOFHitTypes.LIGHT_CHOP.get(),{0.25},soundEvent = SoundEvents.PLAYER_ATTACK_STRONG,
                whenTargetAttacked =  { o1, o2, buffer, attackSystem, _ ->
                    val target = o2.body.owner as? LivingEntity ?: return@AnimBoxAttackComponent
                    grab = target
                },
                fightSpiritModifier = null
            )
            { time in 0.4..0.6 }
        )
        addComponent(AnimPreInputAcceptComponent(0.7, entity.getPreInput(), startAnim))

        addComponent(AnimPreInputAcceptComponent(0.75, entity.getPreInput(), pullAnim))

        addComponent(StuckEffectComponent(5, 0.05) { hitAnim.time in 0.60..0.90 })
        addComponent(AnimBoxAttackComponent(entity, hitAnim, SOFHitTypes.KNOCKDOWN_SWIPE.get(),soundEvent = SoundEvents.PLAYER_ATTACK_CRIT,
            fightSpiritModifier = null
        ) { time in 0.5..0.90 })
    }

    override fun onActivate() {
        super.onActivate()

        grab = null
        holder.animController.setAnimation(startAnim, 0)
    }

    override fun onUpdate() {
        super.onUpdate()
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)

        if (allAnims.all { it.isCancelled }) end()
    }

    override fun onEnd() {
        super.onEnd()
        entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)

        grab?.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
        grab?.putFlag(SparkFlags.SILENCE, false)
        grab?.putFlag(SparkFlags.DISARM, false)
        grab?.putFlag(SparkFlags.DISABLE_PRE_INPUT, false)
        grab = null
    }

}