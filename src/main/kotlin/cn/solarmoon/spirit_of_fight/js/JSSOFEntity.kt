package cn.solarmoon.spirit_of_fight.js


import cn.solarmoon.spark_core.entity.getRelativeVector
import cn.solarmoon.spark_core.js.extension.JSEntity
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.registry.common.SparkStateMachineRegister
import cn.solarmoon.spark_core.state_machine.presets.PlayerBaseAnimStateMachine
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.poise_system.HitType
import cn.solarmoon.spirit_of_fight.skill.StunHandler
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import org.mozilla.javascript.NativeArray
import ru.nsk.kstatemachine.statemachine.processEventBlocking
import kotlin.math.PI
import kotlin.math.atan2

interface JSSOFEntity: JSEntity {

    fun sofCommonAttack(target: Entity, hitName: String, poise: Int, fs: Int) {
        val entity = this.entity
        target.hurtData.write(EntityHitApplier.HIT_TYPE, HitType(hitName, poise, fs))
        super.commonAttack(target)
    }

    fun addFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.addStage(value / 2)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun removeFightSpirit(value: Int) {
        val level = entity.level()
        if (!level.isClientSide) {
            val fightSpirit = entity.getFightSpirit()
            fightSpirit.removeStage(value)
            fightSpirit.syncToClient(entity.id)
        }
    }

    fun getFightSpirit() = entity.getFightSpirit()

    fun clearGrabs() = entity.grabManager.clear()

    fun toggleWieldStyle() {
        WieldStyle.switch(entity)
        if (entity is Player) entity.getStateMachineHandler(SparkStateMachineRegister.PLAYER_BASE_STATE)?.machine?.processEventBlocking(PlayerBaseAnimStateMachine.ResetEvent)
    }

    fun getWieldStyleName() = entity.wieldStyle.serializedName

    fun setSolid(value: Boolean) {
        val entity = this.entity
        entity.isSolid = value
    }

    fun setGuardEnabled(value: Boolean) {
        val entity = this.entity
        entity.isGuardEnabled = value
    }

    fun setCanUseItem(value: Boolean) {
        entity.canUseItem = value
    }

    fun useOffHandItem() {
        val entity = this.entity
        if (entity is LivingEntity) {
            entity.startUsingItem(InteractionHand.OFF_HAND)
        }
    }

    fun moveBySavedInputVector(move: NativeArray): () -> Unit {
        val entity = entity
        val move = move.toVec3()
        if (entity is Player && entity.isLocalPlayer) {
            val input = (entity as LocalPlayer).savedInput
            val moveVector = input.moveVector
            val angle = atan2(moveVector.y, -moveVector.x) - PI.toFloat() / 2
            val move = move.yRot(angle)
            return { entity.deltaMovement = entity.getRelativeVector(move) }
        } else {
            return { entity.deltaMovement = entity.getRelativeVector(move) }
        }
    }

    fun isRecoveryStun(): Boolean {
        return (entity as? IEntityPatch)?.isRecoveryStun == true
    }

    fun setRecoveryStun(value: Boolean) {
        (entity as? IEntityPatch)?.isRecoveryStun = value
    }

    fun getSwitchAttackCooldown(): Long {
        return (entity as? IEntityPatch)?.switchAttackCooldownUntil ?: 0L
    }

    fun setSwitchAttackCooldown(value: Long) {
        (entity as? IEntityPatch)?.switchAttackCooldownUntil = value
    }

    fun setStunned(ticks: Int) {
        StunHandler.setStunned(entity, ticks, stopAnim = false) // JS controls anim usually, avoiding force stop might be better or required? Relying on default.
        // Actually StunHandler.setStunned usually stops anim, but here we might just want to apply/clear ticks.
        // If ticks is 0, we are clearing.
        if (ticks == 0) (entity as? IEntityPatch)?.stunTicks = 0
    }

}