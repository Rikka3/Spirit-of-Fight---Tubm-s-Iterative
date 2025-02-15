package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.SparkCore
import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.skill.SkillHost
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spark_core.skill.node.BehaviorNode
import cn.solarmoon.spark_core.skill.node.BehaviorNodePayload
import cn.solarmoon.spark_core.skill.node.NodeStatus
import cn.solarmoon.spark_core.skill.node.leaves.EmptyNode
import cn.solarmoon.spark_core.skill.node.leaves.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.node.leaves.PreventYRotComponent
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

class GuardComponent(
    val onSuccessGuard: BehaviorNode = EmptyNode.Success
): BehaviorNode() {

    lateinit var bodies: MutableList<PhysicsRigidBody>
    val pY = PreventYRotComponent()

    init {
        dynamicContainer.addChild(onSuccessGuard)
        dynamicContainer.addChild(pY)
    }

    val onHurt = { event: LivingIncomingDamageEvent ->
        run {
            val victim = event.entity
            if (victim.level().isClientSide || !skill.isActive) return@run
            if (victim !is IEntityAnimatable<*>) return@run
            val damagedBody = event.source.extraData?.damagedBody
            val sourcePos = event.source.sourcePosition
            if (victim == skill.holder) {

                fun guard() {
                    event.source.directEntity?.let { write("attacker", it) }
                    write("on_hurt.time", require<AnimInstance>("animation").time)
                    onSuccessGuard.refresh()
                    onSuccessGuard.tick(skill)
                    doGuard()
                    PacketDistributor.sendToAllPlayers(BehaviorNodePayload(this, CompoundTag().apply { putInt("attacker", event.source.directEntity?.id ?: -1) }))
                    event.isCanceled = true
                }

                bodies.forEach {
                    if (it.collideWithGroups != 0) {
                        if (damagedBody != null) {
                            if (it == damagedBody) {
                                write("damaged_body", damagedBody)
                                guard()
                            }
                        } else if (sourcePos != null) {
                            if (victim.canSee(sourcePos, 150.0)) guard()
                        }
                    }
                }
            }
        }
    }

    override fun sync(host: SkillHost, data: CompoundTag, context: IPayloadContext) {
        if (host !is IEntityAnimatable<*>) return
        val id = data.getInt("attacker").takeIf { it != -1 }
        id?.let { skill.level.getEntity(it) }?.let { write("attacker", it) }
        onSuccessGuard.refresh()
        onSuccessGuard.tick(skill)

        doGuard()
    }

    private fun doGuard() {
        val entity = skill.holder as? IEntityAnimatable<*> ?: return
        val anim = require<AnimInstance>("animation")
        val hurtAnim = entity.newAnimInstance(anim.name + "_hurt") {
            onEnable {
                pY.refresh()
                pY.tick(skill)
            }
            onEnd {
                holder.animController.setAnimation(anim, 0)
                pY.end(skill)
            }
            onSwitch {
                if (it?.name != anim.name) skill.end()
            }
        }
        write("hurt_animation", anim)
        entity.animController.setAnimation(hurtAnim, 0)
    }

    override fun onStart(skill: SkillInstance) {
        require<AnimInstance>("animation").apply {
            onSwitch {
                if (it?.name != name + "_hurt") skill.end()
            }
        }
        bodies = require<MutableList<PhysicsRigidBody>>("rigid_body.guard")
        NeoForge.EVENT_BUS.addListener(onHurt)
    }

    override fun onTick(skill: SkillInstance): NodeStatus {
        return NodeStatus.RUNNING
    }

    override fun onEnd(skill: SkillInstance) {
        read<AnimInstance>("hurt_animation")?.cancel()
        NeoForge.EVENT_BUS.unregister(onHurt)
    }

    override val codec: MapCodec<out BehaviorNode> = CODEC

    override fun copy(): BehaviorNode {
        return GuardComponent(onSuccessGuard.copy())
    }

    companion object {
        val CODEC: MapCodec<GuardComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                BehaviorNode.CODEC.optionalFieldOf("on_success_guard", EmptyNode.Success).forGetter { it.onSuccessGuard }
            ).apply(it, ::GuardComponent)
        }
    }

}