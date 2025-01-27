package cn.solarmoon.spirit_of_fight.skill.concrete.axe

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

class AxeDodge(
    holder: IEntityAnimatable<out LivingEntity>
): DodgeSkill(holder, "axe:dodge") {

    /**
     * 无敌时间
     */
    override fun enableDodge(anim: AnimInstance): Boolean {
        return anim.time in 0.0..0.5
    }

    /**
     * 位移
     */
    override fun getMoveSet(anim: AnimInstance): Vec3? {
        return super.getMoveSet(anim)
    }

    /**
     * 预输入节点
     */
    override fun getSwitchNode(): Double {
        return 1.0
    }

}