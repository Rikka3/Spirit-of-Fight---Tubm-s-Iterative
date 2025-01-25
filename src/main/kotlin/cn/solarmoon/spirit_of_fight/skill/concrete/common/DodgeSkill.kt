package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.phys.thread.laterConsume
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.skill.component.AnimImmunityToDamageComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimMoveSetComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

open class DodgeSkill(
    holder: IEntityAnimatable<out LivingEntity>,
    val animNamePre: String
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    var direction: MoveDirection? = null
    var moveVector: Vec3 = Vec3.ZERO
    var check = true

    open val unBlockedDamageType = mutableListOf<ResourceKey<DamageType>>(
        DamageTypes.IN_FIRE,
        DamageTypes.IN_WALL,
        DamageTypes.DROWN,
        DamageTypes.STARVE
    )

    val dodgeAnimMap = buildMap {
        MoveDirection.entries.forEach {
            put(it, createAnimInstance(getAnimName(it)) {
                shouldTurnBody = true
                onEnable {
                    entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
                }

                onEnd {
                    entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
                    end()
                }
            })
        }
    }

    init {
        dodgeAnimMap.values.forEach {
            addComponent(AnimPreInputAcceptComponent(getSwitchNode(), entity.getPreInput(), it,
                limit = { it != "dodge" },
                extraInputNode = { onEnd { entity.level().laterConsume { it.executeIfPresent("dodge") } } }
            ))
            addComponent(AnimImmunityToDamageComponent(entity, it,
                onSuccessImmunity = {
                    direction?.let { d ->
                        holder.animController.getPlayingAnim(getAnimName(d))?.let {
                            if (it.time in 0.0..0.2 && check) {
                                onPerfectDodge(it, this)
                                check = false
                            }
                        }
                    }
                }
            ) { event -> unBlockedDamageType.all { !event.source.typeHolder().`is`(it) } && getImmunityCondition(this) })
            addComponent(AnimMoveSetComponent(entity, it) { getMoveSet(this) })
        }
    }

    open fun getSwitchNode(): Double = 0.35

    open fun getMoveSet(anim: AnimInstance): Vec3? {
        val mul = 0.65 * (1 - anim.getProgress())
        return Vec3(moveVector.x * mul, entity.deltaMovement.y, moveVector.z * mul)
    }

    open fun getImmunityCondition(anim: AnimInstance): Boolean {
        return anim.time in 0.0..0.35
    }

    open fun onPerfectDodge(anim: AnimInstance, event: LivingIncomingDamageEvent) {
        SparkVisualEffects.SHADOW.addToClient(entity.id)
    }

    override fun onActivate() {
        super.onActivate()
        direction?.let {
            holder.animController.setAnimation(getAnim(it), 0)
        }
    }

    override fun onUpdate() {

    }

    override fun onEnd() {
        check = true
    }

    fun getAnimName(direction: MoveDirection) = "${animNamePre}_$direction"

    fun getAnim(direction: MoveDirection) = dodgeAnimMap[direction]!!

}