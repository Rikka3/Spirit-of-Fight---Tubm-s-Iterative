package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.entity.grab.GrabManager
import cn.solarmoon.spirit_of_fight.poise_system.PoiseData
import net.minecraft.resources.ResourceLocation
import org.joml.Vector3f

interface IEntityPatch {

    var moveDirection: MoveDirection?

    val poise: PoiseData

    val grabManager: GrabManager

    var isHitting: Boolean

    var isKnockedDown: Boolean

    var wieldStyle: WieldStyle

    var isSolid: Boolean

    var isGuardEnabled: Boolean

    var canUseItem: Boolean

    var stunTicks: Int

    var lastDodgeTick: Long
    var lastJumpAttackTick: Long
    var sprintAttackCooldownUntil: Long

    var isRecoveryStun: Boolean
    var switchAttackCooldownUntil: Long
    var comboCooldownUntil: Long

    val skillCooldowns: MutableMap<ResourceLocation, Long>

    // Aerial dive attack fields
    var isAerialDiving: Boolean
    var aerialDiveTarget: Int?
    var aerialDiveDirection: Vector3f?

    fun getActiveBlockSkill(): Any?
    fun setActiveBlockSkill(blockSkill: Any?)
}