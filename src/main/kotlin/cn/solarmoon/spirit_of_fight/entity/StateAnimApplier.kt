package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.TypedAnimation
import cn.solarmoon.spark_core.animation.anim.play.layer.AnimLayerData
import cn.solarmoon.spark_core.animation.anim.play.layer.DefaultLayer
import cn.solarmoon.spark_core.animation.anim.play.layer.getMainLayer
import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spark_core.event.PreInputEvent
import cn.solarmoon.spark_core.physics.presets.callback.SparkCollisionCallback
import cn.solarmoon.spark_core.physics.presets.initWithAnimatedBone
import cn.solarmoon.spark_core.physics.presets.ticker.MoveWithAnimatedBoneTicker
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.registry.common.SparkStateMachineRegister
import cn.solarmoon.spark_core.resource.common.SparkResourcePathBuilder
import cn.solarmoon.spark_core.state_machine.presets.PlayerBaseAnimStateMachine
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.event.OnSkillTreeSetChangeEvent
import cn.solarmoon.spirit_of_fight.js.JSSOFConfig
import cn.solarmoon.spark_core.util.MultiModuleResourceExtractionUtil
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.collision.shapes.CompoundCollisionShape
import com.jme3.bullet.objects.PhysicsRigidBody
import com.jme3.math.Vector3f
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.monster.Vindicator
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import ru.nsk.kstatemachine.statemachine.processEventBlocking

object StateAnimApplier {

    @SubscribeEvent
    private fun transModifier(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        fun set(tag: TagKey<Item>, prefix: String) {
            if (player.mainHandItem.`is`(tag)) {
                val stateName = event.state.name
                val animationPath = SparkResourcePathBuilder.buildAnimationPath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "player", "state.$prefix.${player.wieldStyle.serializedName}.$stateName")
                SparkRegistries.TYPED_ANIMATION[animationPath]?.let { animation ->
                    event.newAnim = animation
                }
            }
        }

        JSSOFConfig.ITEM_STATES.forEach { (tag, prefix) ->
            set(tag, prefix)
        }
    }

    @SubscribeEvent
    private fun weaponChangeResetAnim(event: OnSkillTreeSetChangeEvent) {
        val player = event.player
        player.getStateMachineHandler(SparkStateMachineRegister.PLAYER_BASE_STATE)?.machine?.processEventBlocking(PlayerBaseAnimStateMachine.ResetEvent)
    }

    @SubscribeEvent
    private fun stopUseItem(event: LivingEntityUseItemEvent.Start) {
        val entity = event.entity
        if (!entity.canUseItem) event.isCanceled = true
    }

    @SubscribeEvent
    private fun preInputLock(event: PreInputEvent.Lock) {
        val entity = event.preInput.holder as? Entity ?: return
        entity.canUseItem = false
    }

    @SubscribeEvent
    private fun preInputUnlock(event: PreInputEvent.Unlock) {
        val entity = event.preInput.holder as? Entity ?: return
        entity.canUseItem = true
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private fun onRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        val player = event.entity
        if (player.level().isClientSide) return

        if (isPlaceableBlock(event.itemStack)) {
            playBlockAnimation(player, "sof_use_anim.place")
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val player = event.entity
        if (player.level().isClientSide) return

        val blockState = player.level().getBlockState(event.pos)
        if (!blockState.isAir) {
            playBlockAnimation(player, "sof_use_anim.break")
        }
    }

    private fun isPlaceableBlock(item: ItemStack): Boolean {
        return item.item is BlockItem
    }

    private fun playBlockAnimation(player: Player, animationName: String) {
        try {
            val animatable = (player as Any) as? IEntityAnimatable<*> ?: return

            val normalized = MultiModuleResourceExtractionUtil.normalizeResourceName(animationName)
            // Use MOD_ID/animations/player/sof_use_anim as the resource ID part if we want to be very specific
            // But spark_core usually just uses the name if it's already loaded.
            val keyPath = "${SpiritOfFight.MOD_ID}/animations/player/$normalized"
            val keyRl = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, keyPath)
            val key = ResourceKey.create(SparkRegistries.TYPED_ANIMATION.key(), keyRl)

            // Check if animation already exists
            val existingAnimation = SparkRegistries.TYPED_ANIMATION[key]
            if (existingAnimation != null) {
                existingAnimation.play(animatable, DefaultLayer.MAIN_LAYER, AnimLayerData(enterTransitionTime = 0))
                existingAnimation.playToClient(animatable, DefaultLayer.MAIN_LAYER, AnimLayerData(enterTransitionTime = 0))
                return
            }

            // Create animation if not exists
            val index = AnimIndex(
                ResourceLocation.withDefaultNamespace("player"),
                normalized,
                false
            )
            
            val ta = TypedAnimation(index) {
                // No special callback needed for block animations
            }

            SparkRegistries.TYPED_ANIMATION.registerDynamic(
                key = keyRl, 
                value = ta, 
                moduleId = SpiritOfFight.MOD_ID, 
                replace = true,
                triggerCallback = true
            )

            ta.play(animatable, DefaultLayer.MAIN_LAYER, AnimLayerData(enterTransitionTime = 0))
            ta.playToClient(animatable, DefaultLayer.MAIN_LAYER, AnimLayerData(enterTransitionTime = 0))
        } catch (e: Exception) {
            SpiritOfFight.LOGGER.error("Failed to play block animation $animationName: ${e.message}")
        }
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
        if (entity is Player) {
            entity.modelIndex.modelPath = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, "spirit_of_fight/models/player")
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