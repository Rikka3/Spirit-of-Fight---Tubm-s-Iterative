package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.getLateralSide
import cn.solarmoon.spark_core.entity.knockBackRelativeView
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spark_core.skill.SkillPayload
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.skill.component.AnimGuardComponent
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import org.ode4j.ode.DBody

open class ParrySkill(
    holder: IEntityAnimatable<out LivingEntity>,
    animName: String,
    guardBody: DBody = holder.animatable.getPatch().getMainGuardBody()
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    val parryAnim = createAnimInstance(animName) {
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
        addComponent(AnimGuardComponent(entity, parryAnim,
            body = guardBody,
            onSuccessGuard = { attackerPos, event ->
                event.source.directEntity?.let {
                    parry(attackerPos, it, event)
                    event.isCanceled = true
                }
            },
            enableGuard = { shouldEnableParryBox(this) }
            ))
    }

    open fun shouldEnableParryBox(anim: AnimInstance): Boolean {
        return anim.time in 0.0..0.25
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(parryAnim, 0)
    }

    open fun parry(attackerPos: Vec3, attacker: Entity, event: LivingIncomingDamageEvent) {
        val side = entity.getLateralSide(attackerPos, true)
        val animName = "common:parried_$side"
        if (attacker is IEntityAnimatable<*> && attacker.animations.hasAnimation(animName)) {
            playParriedAnim(side, attacker)
            PacketDistributor.sendToAllPlayers(SkillPayload(entity.id, name, CompoundTag().apply { putInt("t", attacker.id); putInt("s", side.ordinal) }))
            when(side) {
                Side.LEFT -> PacketDistributor.sendToAllPlayers(ClientOperationPayload(entity.id, "parried_left", Vec3.ZERO, attacker.id))
                else -> PacketDistributor.sendToAllPlayers(ClientOperationPayload(entity.id, "parried_right", Vec3.ZERO, attacker.id))
            }
        } else if (attacker is LivingEntity) {
            attacker.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 9, false, false, false))
            attacker.knockBackRelativeView(entity, 0.5)
        }
    }

    fun playParriedAnim(side: Side, attacker: IEntityAnimatable<*>) {
        attacker.animController.setAnimation("common:parried_$side", 0) {
            val entity = attacker.animatable

            onEnable {
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, true)
                entity.putFlag(SparkFlags.DISARM, true)
                entity.putFlag(SparkFlags.SILENCE, true)
            }

            onEnd {
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
                entity.putFlag(SparkFlags.DISABLE_PRE_INPUT, false)
                entity.putFlag(SparkFlags.DISARM, false)
                entity.putFlag(SparkFlags.SILENCE, false)
            }

        }
    }

    override fun sync(entity: Entity, data: CompoundTag, context: IPayloadContext) {
        val level = entity.level()
        val attacker = level.getEntity(data.getInt("t")) as? IEntityAnimatable<*> ?: return
        val side = Side.entries[data.getInt("s")]
        playParriedAnim(side, attacker)
    }

}