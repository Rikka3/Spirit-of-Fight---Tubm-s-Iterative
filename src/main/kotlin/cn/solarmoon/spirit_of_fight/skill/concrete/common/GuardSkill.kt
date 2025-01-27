package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.animation.anim.play.BlendAnimation
import cn.solarmoon.spark_core.entity.attack.getExtraData
import cn.solarmoon.spark_core.entity.getLateralSide
import cn.solarmoon.spark_core.entity.knockBackRelativeView
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.registry.common.SparkTypedAnimations
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spark_core.skill.SkillPayload
import cn.solarmoon.spark_core.util.Side
import cn.solarmoon.spirit_of_fight.sync.MovePayload
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import cn.solarmoon.spirit_of_fight.hit.getHitType
import cn.solarmoon.spirit_of_fight.skill.component.AnimGuardComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import cn.solarmoon.spirit_of_fight.sync.ClientOperationPayload
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
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
    enableParry: Boolean,
    guardBody: DBody = holder.animatable.getPatch().getMainGuardBody()
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    var isStanding = false
        private set
    var isBacking = false
        private set

    open val guardSound = SoundEvents.SHIELD_BLOCK
    open val parrySound = SoundEvents.SHIELD_BLOCK

    val guardAnim = createAnimInstance(animNamePre) {
        shouldTurnBody = true

        onEnable {
            isStanding = true
        }

        onTick {
            if (!entity.level().isClientSide) {
                if (entity.knownMovement.horizontalDistance() > 0.01) {
                    holder.animController.blendSpace.putIfAbsent(
                        "guardMix",
                        BlendAnimation(SparkTypedAnimations.WALK.get().create(holder).apply { speed = 0.75 }, 1.0, listOf("rightArm", "leftArm", "rightItem", "leftItem"))
                    )
                    PacketDistributor.sendToAllPlayers(SkillPayload(entity.id, this@GuardSkill.name, CompoundTag().apply { putInt("guard_mix", 1) }))
                }
                else {
                    holder.animController.blendSpace.remove("guardMix")
                    PacketDistributor.sendToAllPlayers(SkillPayload(entity.id, this@GuardSkill.name, CompoundTag().apply { putInt("guard_mix", 2) }))
                }
            }
        }

        onEnd {
            isStanding = false
            holder.animController.blendSpace.remove("guardMix")
        }

        onSwitch {
            if (it?.name !in listOf(hurtAnim.name, parryAnimName)) end()
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
            if (it?.name != guardAnim.name) end()
        }
    }

    val parryAnimName = animNamePre + "_parry"

    val parryAnim = lazy {
        createAnimInstance(parryAnimName) {
            shouldTurnBody = true

            onEnable {
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
            }

            onEnd {
                entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
                end()
            }
        }
    }

    init {
        if (enableParry) parryAnim.value
        addComponent(AnimGuardComponent(entity, guardAnim, guardBody,
            onSuccessGuard = { attackerPos, event ->
                if (shouldPreventGuard(event)) return@AnimGuardComponent

                if (guardAnim.time in 0.0..0.15 && enableParry) {
                    entity.level().playSound(null, entity.onPos.above(), parrySound, SoundSource.PLAYERS)
                    holder.animController.setAnimation(parryAnim.value, 0)
                    PacketDistributor.sendToAllPlayers(SkillPayload(entity.id, name, CompoundTag().apply { putBoolean("parry", true) }))
                    event.source.directEntity?.let {
                        doParry(attackerPos, it, event)
                        event.isCanceled = true
                    }
                } else {
                    // 未在播放击退动画续上击退动画
                    entity.level().playSound(null, entity.onPos.above(), guardSound, SoundSource.PLAYERS)
                    playHurtAnim()
                    PacketDistributor.sendToAllPlayers(SkillPayload(entity.id, name, CompoundTag()))

                    // 击退
                    doGuard(attackerPos, event)
                }
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

    open fun doParry(attackerPos: Vec3, attacker: Entity, event: LivingIncomingDamageEvent) {
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

    open fun doGuard(attackerPos: Vec3, event: LivingIncomingDamageEvent) {
        val v = Vec3(entity.x - attackerPos.x, entity.y - attackerPos.y, entity.z - attackerPos.z).normalize().div(2.0)
        MovePayload.moveEntityInClient(entity.id, v)
        event.isCanceled = true
    }

    fun playHurtAnim() {
        hurtAnim.refresh()
        holder.animController.setAnimation(hurtAnim, 0)
    }

    override fun onEnd() {
        super.onEnd()
        hurtAnim.cancel()
        guardAnim.cancel()
    }

    override fun sync(entity: Entity, data: CompoundTag, context: IPayloadContext) {
        if (data.getBoolean("parry")) {
            holder.animController.setAnimation(parryAnim.value, 0)
        } else {
            val op = data.getInt("guard_mix")
            when(op) {
                1 -> if (entity is IEntityAnimatable<*>) entity.animController.blendSpace.putIfAbsent(
                    "guardMix",
                    BlendAnimation(SparkTypedAnimations.WALK.get().create(entity).apply { speed = 0.75 }, 1.0, listOf("rightArm", "leftArm", "rightItem", "leftItem"))
                )
                2 -> if (entity is IEntityAnimatable<*>) entity.animController.blendSpace.remove("guardMix")
                else -> {
                    val level = entity.level()
                    val attacker = level.getEntity(data.getInt("t")) as? IEntityAnimatable<*> ?: let { playHurtAnim(); return }
                    val side = Side.entries[data.getInt("s")]
                    playParriedAnim(side, attacker)
                }
            }
        }
    }

}