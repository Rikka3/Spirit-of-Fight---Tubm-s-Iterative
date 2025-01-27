package cn.solarmoon.spirit_of_fight.skill.concrete.baimei

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.flag.SparkFlags
import cn.solarmoon.spark_core.flag.putFlag
import cn.solarmoon.spark_core.phys.thread.laterConsume
import cn.solarmoon.spark_core.registry.common.SparkVisualEffects
import cn.solarmoon.spark_core.skill.EntityAnimSkill
import cn.solarmoon.spirit_of_fight.skill.component.AnimImmunityToDamageComponent
import cn.solarmoon.spirit_of_fight.skill.component.AnimPreInputAcceptComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

open class BaimeiSquatDodge(
    holder: IEntityAnimatable<out LivingEntity>,
): EntityAnimSkill<LivingEntity, IEntityAnimatable<out LivingEntity>>(holder) {

    var check = true

    open val unBlockedDamageType = mutableListOf<ResourceKey<DamageType>>(
        DamageTypes.IN_FIRE,
        DamageTypes.IN_WALL,
        DamageTypes.DROWN,
        DamageTypes.STARVE
    )

    val dodgeAnim = createAnimInstance("baimei:dodge_shift") {
        shouldTurnBody = true
        onEnable {
            check = true
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, true)
        }

        onEnd {
            entity.putFlag(SparkFlags.MOVE_INPUT_FREEZE, false)
            end()
        }
    }

    init {
        addComponent(AnimPreInputAcceptComponent(0.3, entity.getPreInput(), dodgeAnim,
            limit = { it != "dodge" },
            extraInputNode = { onEnd { entity.level().laterConsume { it.executeIfPresent("dodge") } } }
        ))
        addComponent(AnimImmunityToDamageComponent(entity, dodgeAnim,
            onSuccessImmunity = {
                if (enableDodge() && check) {
                    onPerfectDodge(this)
                    check = false
                }
            }
        ) { event -> unBlockedDamageType.all { !event.source.typeHolder().`is`(it) } && getImmunityCondition(this) })
    }

    override fun onActivate() {
        super.onActivate()
        holder.animController.setAnimation(dodgeAnim, 2)
    }

    open fun enableDodge(): Boolean {
        return dodgeAnim.time in 0.0..0.2
    }

    open fun getImmunityCondition(anim: AnimInstance): Boolean {
        return anim.time in 0.0..0.25
    }

    open fun onPerfectDodge(event: LivingIncomingDamageEvent) {
        SparkVisualEffects.SHADOW.addToClient(entity.id)
    }

}