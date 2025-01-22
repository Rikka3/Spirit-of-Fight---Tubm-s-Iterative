package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.getExtraData
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.phys.toVec3
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.SkillComponent
import cn.solarmoon.spirit_of_fight.feature.fight_skill.skill.TriggeredSkillComponent
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFBodyTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import org.ode4j.ode.DBody

class AnimGuardComponent(
    val entity: LivingEntity,
    val anim: AnimInstance,
    val body: DBody = entity.getPatch().getMainGuardBody(),
    val vanillaGuardRange: Double = 150.0,
    val unblockableDamageTypes: HashSet<ResourceKey<DamageType>> = hashSetOf(DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION),
    private val onSuccessGuard: (Vec3, LivingIncomingDamageEvent) -> Unit = { a, b -> },
    private val enableGuard: (AnimInstance.() -> Boolean)? = null
): TriggeredSkillComponent {

    var isEnabled = false
        private set

    override fun start() {
        anim.shouldTurnBody = true

        if (enableGuard == null) {
            anim.onEnable {
                body.enable()
                isEnabled = true
            }

            anim.onEnd {
                stop()
            }
        } else {
            anim.onTick {
                if (enableGuard.invoke(anim)) {
                    body.enable()
                } else body.disable()
            }
        }
    }

    fun onHurt(event: LivingIncomingDamageEvent) {
        if (unblockableDamageTypes.any { event.source.`is`(it) }) return
        if (body.isEnabled == true) {
            val damageSource = event.source
            SparkVisualEffects.CAMERA_SHAKE.shakeToClient(entity, 2, 0.5f)
            // 对于原版生物，只要在一个扇形范围内即可，对于lib的obb碰撞，则判断是否相交，同时如果受击数据不为空，那么以受击数据为准
            val attackedData = damageSource.getExtraData()
            // 如果受击数据里有guard，则免疫此次攻击
            val isBoxInteract = attackedData != null && attackedData.damagedBody?.type == SOFBodyTypes.GUARD.get()
            // 如果受到box的攻击，位移以box中心为准，否则以直接攻击者的坐标位置为准
            val targetPos = attackedData?.damageBox?.position?.toVec3() ?: damageSource.sourcePosition ?: return
            // 如果受到box的攻击，按防守盒是否被碰撞为准，否则以攻击者的坐标位置是否在指定扇形范围内为准
            val attackedCheck = if (attackedData != null) isBoxInteract else entity.canSee(targetPos, vanillaGuardRange)
            if (attackedCheck) {
                onSuccessGuard(targetPos, event)
            }
        }
    }

    override fun stop() {
        body.disable()
        isEnabled = false
    }

}