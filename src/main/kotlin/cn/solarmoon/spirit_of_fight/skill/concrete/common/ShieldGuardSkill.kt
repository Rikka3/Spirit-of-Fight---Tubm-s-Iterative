package cn.solarmoon.spirit_of_fight.skill.concrete.common

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spirit_of_fight.feature.body.createSkillAttackAnimBody
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import org.ode4j.math.DVector3
import org.ode4j.ode.DBody
import org.ode4j.ode.DBox

class ShieldGuardSkill(
    holder: IEntityAnimatable<out LivingEntity>
): GuardSkill(holder, "shield:guard", createSkillAttackAnimBody("leftItem", holder, holder.animatable.level(), holder.animatable.getPatch().offAttackSystem) {
    (firstGeom as? DBox)?.let {
        it.lengths = DVector3(0.5, 1.0, 1.55)
        it.offsetPosition = DVector3(-0.25, 0.0, -0.25)
    }
}) {

    override fun doGuard(attackerPos: Vec3, event: LivingIncomingDamageEvent) {
        super.doGuard(attackerPos, event)

        val shield = entity.offhandItem
        val level = entity.level()
        if (shield.canPerformAction(ItemAbilities.SHIELD_BLOCK)) {
            shield.hurtAndBreak(1, entity, EquipmentSlot.OFFHAND)
            level.playSound(null, entity.onPos.above(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F)
            event.isCanceled = true
        }
    }

    override fun shouldPreventGuard(event: LivingIncomingDamageEvent): Boolean {
        return false
    }

}