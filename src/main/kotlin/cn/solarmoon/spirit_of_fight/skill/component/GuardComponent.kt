package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.entity.canSee
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class GuardComponent: SkillComponent() {

    lateinit var bodies: MutableList<PhysicsRigidBody>

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return GuardComponent()
    }

    @SubscribeEvent
    private fun onHurt(event: LivingIncomingDamageEvent) {
        val victim = event.entity
        if (level.isClientSide) return
        val damagedBody = event.source.extraData?.damagedBody
        val sourcePos = event.source.sourcePosition
        if (victim == skill.holder) {
            bodies.forEach {
                if (it.collideWithGroups != 0) {
                    if (damagedBody != null) {
                        if (it == damagedBody) event.isCanceled = true
                    } else if (sourcePos != null) {
                        if (victim.canSee(sourcePos, 150.0)) event.isCanceled = true
                    }
                }
            }
        }
    }

    override fun onActive() {
        bodies = requireQuery<MutableList<PhysicsRigidBody>>("rigid_body.guard")
        NeoForge.EVENT_BUS.register(this)
    }

    override fun onUpdate() {}

    override fun onEnd() {
        NeoForge.EVENT_BUS.unregister(this)
    }

    companion object {
        val CODEC: MapCodec<GuardComponent> = RecordCodecBuilder.mapCodec {
            it.stable(GuardComponent())
        }
    }

}