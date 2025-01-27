package cn.solarmoon.spirit_of_fight.skill.concrete.barehand

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

class BareHandDodge(
    holder: IEntityAnimatable<out LivingEntity>
): DodgeSkill(holder, "barehand:dodge") {

    /**
     * 无敌时间
     */
    override fun enableDodge(anim: AnimInstance): Boolean {
        return anim.time in 0.0..0.4
    }

    /**
     * 位移
     */
    override fun getMoveSet(anim: AnimInstance): Vec3? {
        val mul = 0.45 * (1 - anim.getProgress())
        return Vec3(moveVector.x * mul, entity.deltaMovement.y, moveVector.z * mul)
    }

    /**
     * 预输入节点
     */
    override fun getSwitchNode(): Double {
        return 1.0
    }

}