package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spark_core.physics.presets.callback.SparkCollisionCallback
import cn.solarmoon.spark_core.physics.presets.initWithAnimatedBone
import cn.solarmoon.spark_core.physics.presets.ticker.MoveWithAnimatedBoneTicker
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.registry.common.SparkStateMachineRegister
import cn.solarmoon.spark_core.resource.common.SparkResourcePathBuilder
import cn.solarmoon.spark_core.state_machine.presets.PlayerBaseAnimStateMachine
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.event.OnSkillTreeSetChangeEvent
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.collision.shapes.CompoundCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.monster.Vindicator
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import ru.nsk.kstatemachine.statemachine.processEventBlocking

object StateAnimApplier {

    @SubscribeEvent
    private fun transModifier(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        fun set(tag: TagKey<Item>, prefix: String) {
            if (player.mainHandItem.`is`(tag)) {
                val stateName = event.state.name
                val animationPath = SparkResourcePathBuilder.buildAnimationPath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "player", "state.$prefix.${player.wieldStyle.serializedName}.$stateName")
                when(stateName) {
                    "land.idle", "land.move", "land.move_back", "land.sprint" -> event.newAnim = SparkRegistries.TYPED_ANIMATION[animationPath]
                }
            }
        }

        set(ItemTags.SWORDS, "sword")
        set(SOFItemTags.FORGE_HAMMERS, "hammer")
        set(SOFItemTags.FORGE_GLOVES, "gloves")
    }

    @SubscribeEvent
    private fun weaponChangeResetAnim(event: OnSkillTreeSetChangeEvent) {
        val player = event.player
        player.getStateMachineHandler(SparkStateMachineRegister.PLAYER_BASE_STATE)?.machine?.processEventBlocking(PlayerBaseAnimStateMachine.ResetEvent)
    }

    @SubscribeEvent
    private fun add(event: EntityJoinLevelEvent) {
        val entity = event.entity
        val level = entity.level()
        if (entity is Zombie || entity is Vindicator) {
            entity.modelIndex.modelPath = ResourceLocation.fromNamespaceAndPath(SparkCore.MOD_ID, "spark_core/models/zombie")
            entity.model.bones.values.filterNot { it.name in listOf("rightItem", "leftItem") }.forEach { bone ->
                val body = PhysicsRigidBody(bone.name, entity, CompoundCollisionShape())

                entity.bindBody(body, level.physicsLevel, allowOverride = true) {
                    (this.collisionShape as CompoundCollisionShape).initWithAnimatedBone(bone)
                    this.isContactResponse = false
                    this.setGravity(Vector3f.ZERO)
                    this.setEnableSleep(false)
                    this.isKinematic = true
                    this.collideWithGroups = PhysicsCollisionObject.COLLISION_GROUP_OBJECT or PhysicsCollisionObject.COLLISION_GROUP_BLOCK
                    this.addPhysicsTicker(MoveWithAnimatedBoneTicker(bone.name))
                    this.addCollisionCallback(SparkCollisionCallback(
                        owner = entity,
                        cbName = body.name,
                        collisionBoxId = body.name
                    ))
                }
            }
        }
    }

}