package cn.solarmoon.spirit_of_fight.feature.body

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.phys.AttackCallBack
import cn.solarmoon.spark_core.phys.BodyType
import cn.solarmoon.spark_core.phys.createAnimatedPivotBody
import cn.solarmoon.spark_core.phys.createEntityAnimatedAttackBody
import cn.solarmoon.spark_core.phys.thread.getPhysLevel
import cn.solarmoon.spark_core.phys.toDVector3
import cn.solarmoon.spark_core.registry.common.SparkBodyTypes
import cn.solarmoon.spark_core.skill.controller.getSkillController
import cn.solarmoon.spirit_of_fight.registry.common.SOFBodyTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import org.ode4j.math.DVector3
import org.ode4j.ode.DBody
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom
import org.ode4j.ode.OdeHelper

fun createGuardAnimBody(boneName: String, owner: IEntityAnimatable<*>, level: Level, type: BodyType = SOFBodyTypes.GUARD.get(), provider: DBody.() -> Unit = {}) =
    createAnimatedPivotBody(boneName, type, owner, level) {

        disable()

        provider.invoke(this)
    }

fun createSkillAttackAnimBody(boneName: String, owner: IEntityAnimatable<*>, level: Level, attackSystem: AttackSystem, type: BodyType = SOFBodyTypes.ATTACK.get(), provider: DBody.() -> Unit = {}) =
    createEntityAnimatedAttackBody(boneName, type, owner, level, object : AttackCallBack(attackSystem) {

        val entity get() = owner.animatable

        override fun whenAboutToAttack(o1: DGeom, o2: DGeom, buffer: DContactBuffer) {
            entity.getSkillController()?.allActiveSkills?.forEach {
                it.components.forEach {
                    if (it is AnimBoxAttackComponent) it.whenAboutToAttack(o1, o2, buffer, attackSystem)
                }
            }
        }

        override fun whenTargetAttacked(o1: DGeom, o2: DGeom, buffer: DContactBuffer) {
            entity.getSkillController()?.allActiveSkills?.forEach {
                it.components.forEach {
                    if (it is AnimBoxAttackComponent) it.whenTargetAttacked(o1, o2, buffer, attackSystem)
                }
            }
        }
    }) {


        provider.invoke(this)
    }

fun createEmptyBody(owner: Entity, level: Level, provider: DBody.() -> Unit = {}) =
    OdeHelper.createBody(SparkBodyTypes.CUSTOM.get(), owner, "NONE", false, level.getPhysLevel().world).apply {

        disable()

        provider.invoke(this)
    }