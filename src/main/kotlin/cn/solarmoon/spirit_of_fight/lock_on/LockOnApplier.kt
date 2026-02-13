package cn.solarmoon.spirit_of_fight.lock_on

import cn.solarmoon.spark_core.entity.smoothLookAt
import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.local_control.onEvent
import cn.solarmoon.spark_core.util.toDegrees
import cn.solarmoon.spark_core.util.toRadians
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import net.minecraft.world.phys.EntityHitResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent
import net.neoforged.neoforge.client.event.ViewportEvent
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LockOnApplier {

    @SubscribeEvent
    private fun adjustTarget(event: MovementInputUpdateEvent) {
        val hit = Minecraft.getInstance().gameRenderer.pick(event.entity, LockOnController.lookDistance, LockOnController.lookDistance, 1f)
        SOFKeyMappings.LOCK_ON.get().onEvent(KeyEvent.PRESS_ONCE) {
            if (hit is EntityHitResult && !LockOnController.hasTarget) {
                LockOnController.setTarget(hit.entity)
            } else {
                LockOnController.setTarget(null)
            }
            Minecraft.getInstance().hitResult
            true
        }
    }

    @SubscribeEvent
    private fun tick(event: ViewportEvent.ComputeCameraAngles) {
        val player = Minecraft.getInstance().player ?: return

        // 生物死亡后解绑
        if (LockOnController.target?.isRemoved == true) LockOnController.clear()

        LockOnController.target?.let { target ->
            // 距离过远自动解绑
            if (player.distanceTo(target) > LockOnController.lookDistance) {
                LockOnController.clear()
                return@let
            }

            val partialTicks = event.partialTick.toFloat()
            // 获取玩家眼睛位置（摄像机位置）
            val eyes = player.getEyePosition(partialTicks)
            // 获取目标位置（假设已更新为最新位置）
            val targetPos = LockOnController.getLookPos(partialTicks)

            // 计算方向向量
            val dx = targetPos.x - eyes.x
            val dy = targetPos.y - eyes.y
            val dz = targetPos.z - eyes.z

            // 计算水平距离
            val horizontalDist = sqrt(dx * dx + dz * dz).toFloat()

            // 计算偏航角 (yaw) 和俯仰角 (pitch)
            val yaw = (atan2(dz.toDouble(), dx.toDouble()) * (180.0 / Math.PI)).toFloat() - 90.0f
            val pitch = -(atan2(dy.toDouble(), horizontalDist.toDouble()) * (180.0 / Math.PI)).toFloat()

            // 设置摄像机角度
            event.yaw = yaw
            event.pitch = pitch

            if (player.isPlayingSkill) {
                player.smoothLookAt(targetPos, partialTicks)
            }
        }
    }

    @SubscribeEvent
    private fun onMovementInput(event: MovementInputUpdateEvent) {
        val player = event.entity

        if (LockOnController.hasTarget && !player.isPlayingSkill) {
            val input = event.input
            val forward = input.forwardImpulse
            val left = input.leftImpulse

            // 如果没有输入则跳过计算
            if (forward == 0f && left == 0f) return

            // 获取摄像机当前的偏航角（弧度）
            val cameraYaw = Minecraft.getInstance().gameRenderer.mainCamera.yRot.toRadians()

            // 计算相对于摄像机的移动方向
            // 这样无论玩家当前面朝哪个方向，移动都会相对于摄像机视角
            val moveX = left.toDouble()
            val moveZ = forward.toDouble()
            
            // 计算移动方向的角度
            val targetMoveYaw = atan2(-moveX, moveZ) + cameraYaw

            player.yRot = Mth.wrapDegrees(targetMoveYaw.toDegrees().toFloat())
            
            // 保持原有的移动冲力计算
            val relativeYaw = player.yRot - cameraYaw.toDegrees()
            val relativeYawRad = relativeYaw.toRadians()
            
            input.forwardImpulse = (forward * cos(relativeYawRad) - left * sin(relativeYawRad)).toFloat()
            input.leftImpulse = (left * cos(relativeYawRad) + forward * sin(relativeYawRad)).toFloat()
        }
    }
}