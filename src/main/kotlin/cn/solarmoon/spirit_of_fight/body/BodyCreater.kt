package cn.solarmoon.spirit_of_fight.body

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.phys.AttackCallBack
import cn.solarmoon.spark_core.phys.BodyType
import cn.solarmoon.spark_core.phys.createAnimatedPivotBody
import cn.solarmoon.spark_core.phys.createEntityAnimatedAttackBody
import cn.solarmoon.spark_core.phys.thread.getPhysLevel
import cn.solarmoon.spark_core.phys.toDMatrix3
import cn.solarmoon.spark_core.phys.toDQuaternion
import cn.solarmoon.spark_core.phys.toDVector3
import cn.solarmoon.spark_core.phys.toRadians
import cn.solarmoon.spark_core.registry.common.SparkBodyTypes
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.controller.getSkillController
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFBodyTypes
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.StuckEffectComponent
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Matrix3d
import org.joml.Quaterniond
import org.ode4j.math.DVector3
import org.ode4j.ode.DBody
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom
import org.ode4j.ode.OdeHelper
import java.awt.Color
import kotlin.toString

fun createGuardAnimBody(boneName: String, owner: IEntityAnimatable<*>, level: Level, type: BodyType = SOFBodyTypes.GUARD.get(), provider: DBody.() -> Unit = {}) =
    createAnimatedPivotBody(boneName, type, owner, level) {

        disable()

        provider.invoke(this)
    }

fun createSkillAttackAnimBody(boneName: String, owner: IEntityAnimatable<*>, level: Level, attackSystem: AttackSystem, type: BodyType = SOFBodyTypes.ATTACK.get(), provider: DBody.() -> Unit = {}) =
    createEntityAnimatedAttackBody(boneName, type, owner, level, object : AttackCallBack(attackSystem) {

        val entity get() = owner.animatable

        override fun whenAboutToAttack(o1: DGeom, o2: DGeom, buffer: DContactBuffer): Boolean {
            if (o2.body.owner == (o1.body.owner as? Entity)?.vehicle) return false
            entity.getSkillController()?.allActiveSkills?.forEach {
                it.components.forEach {
                    if (it is AnimBoxAttackComponent && it.isActive) it.whenAboutToAttack(o1, o2, buffer, attackSystem)
                }
            }
            return true
        }

        override fun whenTargetAttacked(o1: DGeom, o2: DGeom, buffer: DContactBuffer) {
            entity.getSkillController()?.allActiveSkills?.forEach {
                it.components.forEach {
                    if (it is AnimBoxAttackComponent && it.isActive) it.whenTargetAttacked(o1, o2, buffer, attackSystem)
                    if (it is StuckEffectComponent) it.whenTargetAttacked(o1, o2, buffer, attackSystem)
                }
            }
        }
    }) {


        provider.invoke(this)
    }

fun createForwardAttackBox(entity: Entity, distance: Double, attackSystem: AttackSystem, provider: DBody.() -> Unit = {}) =
    createForwardAttackBox(entity, distance, object : AttackCallBack(attackSystem) {

        override fun whenAboutToAttack(o1: DGeom, o2: DGeom, buffer: DContactBuffer): Boolean {
            if (o2.body.owner == (o1.body.owner as? Entity)?.vehicle) return false
            entity.getSkillController()?.allActiveSkills?.forEach {
                it.components.forEach {
                    if (it is AnimBoxAttackComponent && it.isActive) it.whenAboutToAttack(o1, o2, buffer, attackSystem)
                }
            }
            return true
        }

        override fun whenTargetAttacked(o1: DGeom, o2: DGeom, buffer: DContactBuffer) {
            entity.getSkillController()?.allActiveSkills?.forEach {
                it.components.forEach {
                    if (it is AnimBoxAttackComponent && it.isActive) it.whenTargetAttacked(o1, o2, buffer, attackSystem)
                    if (it is StuckEffectComponent) it.whenTargetAttacked(o1, o2, buffer, attackSystem)
                }
            }
        }
    }) {

        provider.invoke(this)
    }

fun createForwardAttackBox(entity: Entity, distance: Double, attackCallBack: AttackCallBack, provider: DBody.() -> Unit = {}) = OdeHelper.createBody(SOFBodyTypes.ATTACK.get(), entity, "hammer_aoe", false, entity.level().getPhysLevel().world).apply {
    val geom = OdeHelper.laterCreateBox(this, entity.level().getPhysLevel().world, DVector3(2.0, 2.0, 2.0))
    val level = entity.level()

    firstGeom.isPassFromCollide = true
    disable()

    onPhysTick {
        val pos = entity.position()
        position = pos.toDVector3()
        rotation = Matrix3d().rotateY(Mth.wrapDegrees(entity.yRot.toDouble()).toRadians()).toDMatrix3()
        geom.offsetPosition = DVector3(0.0, geom.lengths.get1() / 2, distance)
        if (level.isClientSide) SparkVisualEffects.GEOM.getRenderableBox(geom.uuid.toString()).refresh(geom)
    }

    firstGeom.onCollide { o2, buffer ->
        val successAttack = attackCallBack.doAttack(firstGeom, o2, buffer) {
            if (level.isClientSide) {
                SparkVisualEffects.GEOM.getRenderableBox(firstGeom.uuid.toString()).setColor(Color.RED)
                SparkVisualEffects.GEOM.getRenderableBox(o2.uuid.toString()).setColor(Color.RED)
            }

            attackCallBack.whenAboutToAttack(firstGeom, o2, buffer)
        }
        if (successAttack) attackCallBack.whenTargetAttacked(firstGeom, o2, buffer)
    }

    onEnable {
        if (!isEnabled) attackCallBack.attackSystem.reset()
    }

    onDisable {
        attackCallBack.attackSystem.reset()
    }

    provider.invoke(this)
}

fun createEmptyBody(owner: Entity, level: Level, provider: DBody.() -> Unit = {}) =
    OdeHelper.createBody(SparkBodyTypes.CUSTOM.get(), owner, "NONE", false, level.getPhysLevel().world).apply {

        disable()

        provider.invoke(this)
    }