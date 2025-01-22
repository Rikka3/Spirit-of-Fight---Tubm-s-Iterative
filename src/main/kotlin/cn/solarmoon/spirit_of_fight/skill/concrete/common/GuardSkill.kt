package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.attack.getExtraData
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spark_core.skill.SkillPayload
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.MovePayload
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.hit.getHitType
import cn.solarmoon.spirit_of_fight.skill.component.AnimGuardComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import org.ode4j.ode.DBody
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.div

open class GuardSkill(
    holder: IEntityAnimatable<out LivingEntity>,
    animNamePre: String,
    guardBody: DBody = holder.animatable.getPatch().getMainGuardBody()
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    var isStanding = false
        private set
    var isBacking = false
        private set

    val guardAnim = createAnimInstance(animNamePre) {
        shouldTurnBody = true

        onEnable {
            isStanding = true
        }

        onEnd {
            isStanding = false
        }

        // 防止意外情况进入别的动画但未停止该技能
        onSwitch {
            if (isActive() && it?.name != hurtAnim.name) end()
        }
    }

    val hurtAnim: AnimInstance = createAnimInstance("${animNamePre}_hurt") {
        onEnable {
            isBacking = true
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onEnd {
            isBacking = false
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            holder.animController.setAnimation(guardAnim, 0)
        }

        // 防止意外情况进入别的动画但未停止该技能
        onSwitch {
            if (isActive() && it?.name != guardAnim.name) end()
        }
    }

    init {
        addComponent(AnimGuardComponent(entity, guardAnim, guardBody,
            onSuccessGuard = { attackerPos, event ->
                if (shouldPreventGuard(event)) return@AnimGuardComponent

                // 未在播放击退动画续上击退动画
                playHurtAnim()
                PacketDistributor.sendToAllPlayers(SkillPayload(entity.id, name, CompoundTag()))

                // 击退
                doGuard(attackerPos, event)
            }
        ))
        addComponent(AnimGuardComponent(entity, hurtAnim, guardBody,
            onSuccessGuard =  { attackerPos, event ->
                if (shouldPreventGuard(event)) return@AnimGuardComponent

                doGuard(attackerPos, event)
            }
        ))
        addComponent(AnimPreInputAcceptComponent(0.0, entity.getPreInput(), guardAnim, { it == "dodge" || it == "guard_stop" }))
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(guardAnim, 5)
    }

    open fun shouldPreventGuard(event: LivingIncomingDamageEvent): Boolean {
        //对于不可阻挡的伤害类型以及击打力度过大的情况，不会被格挡成功
        return event.source.getExtraData()?.getHitType()?.indefensible == true
    }

    open fun doGuard(attackerPos: Vec3, event: LivingIncomingDamageEvent) {
        val v = Vec3(entity.x - attackerPos.x, entity.y - attackerPos.y, entity.z - attackerPos.z).normalize().div(2.5)
        MovePayload.moveEntityInClient(entity.id, v)
        event.isCanceled = true
    }

    fun playHurtAnim() {
        hurtAnim.refresh()
        holder.animController.setAnimation(hurtAnim, 0)
    }

    override fun onEnd() {
        super.onEnd()
        holder.animController.stopAnimation()
    }

    override fun sync(entity: Entity, data: CompoundTag, context: IPayloadContext) {
        SparkCore.LOGGER.info("233")
        playHurtAnim()
    }

}