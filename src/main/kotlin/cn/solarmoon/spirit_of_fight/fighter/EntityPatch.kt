package cn.solarmoon.spirit_of_fight.fighter

import cn.solarmoon.spark_core.entity.attack.AttackSystem
import cn.solarmoon.spirit_of_fight.body.createEmptyBody
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

open class EntityPatch(
    val entity: Entity
) {

    val mainAttackSystem = AttackSystem(entity)
    val offAttackSystem = AttackSystem(entity)

    protected var mainWeaponAttackBody = lazy { createEmptyBody(entity, entity.level()) }
    protected var offWeaponAttackBody = lazy { createEmptyBody(entity, entity.level()) }
    protected var mainWeaponGuardBody = lazy { createEmptyBody(entity, entity.level()) }
    protected var offWeaponGuardBody = lazy { createEmptyBody(entity, entity.level()) }

    open fun onJoinLevel(level: Level) {}

    open fun getMainAttackBody() = mainWeaponAttackBody.value

    open fun getOffAttackBody() = offWeaponAttackBody.value

    open fun getMainGuardBody() = mainWeaponGuardBody.value

    open fun getOffGuardBody() = offWeaponGuardBody.value

}