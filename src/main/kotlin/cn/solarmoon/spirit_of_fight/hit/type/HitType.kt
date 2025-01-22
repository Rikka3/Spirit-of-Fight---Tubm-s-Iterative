package cn.solarmoon.spirit_of_fight.hit.type

import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spark_core.phys.thread.ClientPhysLevel
import cn.solarmoon.spark_core.phys.thread.getPhysLevel
import cn.solarmoon.spark_core.phys.toDQuaternion
import cn.solarmoon.spark_core.phys.toDVector3
import cn.solarmoon.spark_core.phys.toQuaternionf
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spark_core.visual_effect.common.trail.Trail
import cn.solarmoon.spirit_of_fight.hit.AttackStrength
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import org.joml.Quaterniond
import org.joml.Vector3f
import org.ode4j.ode.DBody
import org.ode4j.ode.DBox
import org.ode4j.ode.DContactBuffer
import org.ode4j.ode.DGeom
import java.awt.Color

/**
 * 攻击类型，目的是更好的描述攻击的种类，比如劈砍/戳刺/上挑等，其中一些关于击打后的方法接调于[cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.AttackAnimSkill]，虽然和直接在skill中书写并无大差，但
 * 此处旨在分离一些固定的、但会因攻击类型而影响到效果的内容，比如屏幕抖动，刀光等固定内容，举例来说比如我们希望对于不可抵挡的攻击统一渲染刀光为红色，那么只在skill中进行修改显然需要重复书写的内容会大大增加，在固定的
 * 攻击类型中修改，则能够以组装的方式在需要的时候挪用即可。
 */
interface HitType {

    val strength: AttackStrength

    val isHeavy get() = strength.value > 1

    /**
     * 此值为true时，该攻击无法被默认格挡阻挡，并且受击动画不会被任何动画覆盖
     */
    val indefensible: Boolean

    /**
     * 例：攻击者发造成攻击时，攻击者若在受击者的正前方，那么[posSide]会返回[Side.FRONT]，如果击打到了左腿，那么[boneName]大概会返回["leftLeg"]，如果攻击是从以受击者视角的左边袭来的，那么[hitSide]会返回[Side.LEFT]
     * @param target 受击目标
     * @param strength 受击力度
     * @param boneName 受击骨骼
     * @param posSide 攻击者所在的相对于受击者的四个朝向（前后左右）
     * @param hitSide 攻击碰撞所在的相对于受击者的**左右**方向
     */
    fun getHitAnimation(target: IAnimatable<*>, strength: AttackStrength, boneName: String, posSide: Side, hitSide: Side): AnimInstance?

    fun whenAttackEntry(body: DBody) {}

    fun whenAttacking(body: DBody) {}

    fun whenAttackExit(body: DBody) {}

    fun whenAboutToAttack(o1: DGeom, o2: DGeom, buffer: DContactBuffer, attackSystem: AttackSystem, damageMultiply: Double) {}

    /**
     * 当攻击到首个目标时调用
     */
    fun whenTargetAttacked(o1: DGeom, o2: DGeom, buffer: DContactBuffer, attackSystem: AttackSystem, damageMultiply: Double) {}

    val simpleName get() = registryKey.path

    val id get() = SOFRegistries.HIT_TYPE.getId(this)

    val registryKey get() = SOFRegistries.HIT_TYPE.getKey(this) ?: throw NullPointerException("${::javaClass.name} 尚未注册")

    val resourceKey get() = SOFRegistries.HIT_TYPE.getResourceKey(this).get()

    val builtInRegistryHolder get() = SOFRegistries.HIT_TYPE.getHolder(resourceKey).get()

    fun `is`(tag: TagKey<HitType>) = builtInRegistryHolder.`is`(tag)

}