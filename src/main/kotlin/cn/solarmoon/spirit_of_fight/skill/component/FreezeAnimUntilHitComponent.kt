package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.payload.SkillComponentPayload
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

class FreezeAnimUntilHitComponent(
    val startTime: Double,
    val speed: Double = 0.0
): SkillComponent() {

    var speedCache = 0.0
    var check = false
    var anim: AnimInstance? = null

    override fun onAttach(): Boolean {
        val animatable = skill.holder as? IAnimatable<*> ?: return false
        speedCache = animatable.animController.getPlayingAnim()?.speed ?: 0.0
        animatable.animController.getPlayingAnim()?.onEvent<AnimEvent.PhysicsTick> {
            anim = this
            if (time >= startTime && !check) {
                check = true
                speed = this@FreezeAnimUntilHitComponent.speed
            }
        }
        return true
    }

    override fun onTargetHurt(event: LivingIncomingDamageEvent) {
        anim?.speed = speedCache
        PacketDistributor.sendToAllPlayers(SkillComponentPayload(this))
    }

    override fun sync(data: CompoundTag, context: IPayloadContext) {
        anim?.speed = speedCache
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<FreezeAnimUntilHitComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.DOUBLE.fieldOf("start_time").forGetter { it.startTime },
                Codec.DOUBLE.optionalFieldOf("speed", 0.0).forGetter { it.speed }
            ).apply(it, ::FreezeAnimUntilHitComponent)
        }
    }

}