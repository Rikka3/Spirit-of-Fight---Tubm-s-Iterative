package cn.solarmoon.spirit_of_fight.skill.controller.component

import cn.solarmoon.spark_core.entity.preinput.PreInput
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.feature.fight_skill.sync.ClientOperationPayload
import cn.solarmoon.spirit_of_fight.fighter.player.PlayerLocalController
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.skill.concrete.common.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.controller.SkillControlComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.player.Input
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.math.PI
import kotlin.math.atan2

class DodgeControlComponent(
    val dodge: DodgeSkill
): SkillControlComponent {

    override val name: String = "dodge"

    override fun localControl(
        controller: PlayerLocalController,
        player: LocalPlayer,
        preInput: PreInput,
        input: Input
    ): Boolean {
        return controller.onPressOnce(SOFKeyMappings.DODGE) {
            if (player.onGround()) {
                val camera = Minecraft.getInstance().gameRenderer.mainCamera
                val angle = atan2(input.moveVector.y, -input.moveVector.x) - PI.toFloat() / 2
                val v = Vec3.directionFromRotation(0f, camera.yRot).yRot(angle)
                val d = MoveDirection.getByInput(input) ?: return@onPressOnce false
                preInput.setInput("dodge", 5) {
                    dodge.moveVector = v
                    dodge.direction = d
                    dodge.activate()
                    sendPackage(player.id, d.id, v)
                }
                true
            }
            else false
        }
    }

    override fun serverControl(
        entity: Entity,
        payload: ClientOperationPayload,
        context: IPayloadContext
    ) {
        dodge.direction = MoveDirection.getById(payload.id)
        dodge.moveVector = payload.moveVector
        dodge.activate()
    }

}