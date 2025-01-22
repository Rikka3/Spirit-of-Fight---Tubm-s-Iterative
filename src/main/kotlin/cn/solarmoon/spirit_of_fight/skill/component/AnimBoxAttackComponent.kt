package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.entity.getAttackAnimSpeed
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.controller.getTypedSkillController
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.TriggeredSkillComponent
import cn.solarmoon.spirit_of_fight.feature.fight_skill.spirit.commonAdd
import cn.solarmoon.spirit_of_fight.feature.fight_skill.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.hit.type.HitType
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.flag.SOFFlags
import cn.solarmoon.spirit_of_fight.skill.controller.FightSkillController
import net.minecraft.world.entity.Entity
import org.ode4j.ode.DBody
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom
import kotlin.properties.Delegates

class AnimBoxAttackComponent(
    val entity: Entity,
    val anim: AnimInstance,
    val hitType: HitType,
    private val damageMultiplier: AnimInstance.() -> Double = { 1.0 },
    val baseAttackSpeed: (() -> Double?)? = { entity.getTypedSkillController<FightSkillController<*>>()?.baseAttackSpeed },
    val body: DBody = entity.getPatch().getMainAttackBody(),
    val whenAboutToAttack: (DGeom, DGeom, DContactBuffer, AttackSystem, Double) -> Unit = { a,b,c,d,e -> },
    val whenTargetAttacked: (DGeom, DGeom, DContactBuffer, AttackSystem, Double) -> Unit = { a,b,c,d,e -> },
    val fightSpiritModifier: ((DGeom, DGeom, DContactBuffer, AttackSystem, Double) -> Unit)? = { o1, o2, buffer, attackSystem, dm -> entity.getFightSpirit().commonAdd(o1, o2, dm) },
    val enableAttack: AnimInstance.() -> Boolean
): TriggeredSkillComponent {

    val damageMultiply get() = damageMultiplier.invoke(anim)

    var isActive = false
        private set

    private var newAttackCheck by Delegates.observable(false) { _, old, new -> if (old != new) if (new) whenAttackEntry() else whenAttackExit() }

    override fun start() {
        baseAttackSpeed?.let { it.invoke()?.toFloat()?.let { baseSpeedValue -> entity.getAttackAnimSpeed(baseSpeedValue) } }?.let { anim.speed = it.toDouble() }

        anim.onTick {
            newAttackCheck = enableAttack.invoke(this)

            if (newAttackCheck) {
                whenAttacking()
            }
        }

        anim.onEnd {
            stop()
        }
    }

    fun whenAttackEntry() {
        isActive = true
        body.enable()
        entity.putFlag(SOFFlags.ATTACKING, true)
        hitType.whenAttackEntry(body)
    }

    fun whenAttacking() {
        hitType.whenAttacking(body)
    }

    fun whenAttackExit() {
        stop()

        hitType.whenAttackExit(body)
    }

    fun whenAboutToAttack(o1: DGeom, o2: DGeom, buffer: DContactBuffer, attackSystem: AttackSystem) {
        whenAboutToAttack.invoke(o1, o2, buffer, attackSystem, damageMultiply)
        hitType.whenAboutToAttack(o1, o2, buffer, attackSystem, damageMultiply)
    }

    fun whenTargetAttacked(o1: DGeom, o2: DGeom, buffer: DContactBuffer, attackSystem: AttackSystem) {
        whenTargetAttacked.invoke(o1, o2, buffer, attackSystem, damageMultiply)
        fightSpiritModifier?.invoke(o1, o2, buffer, attackSystem, damageMultiply)
        hitType.whenTargetAttacked(o1, o2, buffer, attackSystem, damageMultiply)
    }

    override fun stop() {
        isActive = false
        body.disable()
        entity.putFlag(SOFFlags.ATTACKING, false)
    }

}