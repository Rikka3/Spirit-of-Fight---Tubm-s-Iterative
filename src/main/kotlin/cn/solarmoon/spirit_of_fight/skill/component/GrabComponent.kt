package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.skill.SkillTimeLine
import cn.solarmoon.spark_core.skill.component.AddTargetMovementComponent
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class GrabComponent(
    val bone: String,
    val offset: Vec3,
    val activeTime: List<SkillTimeLine.Stamp> = listOf()
): SkillComponent() {

    override fun onAttach(): Boolean {
        val animatable = skill.holder as? IEntityAnimatable<*> ?: return false
        val entity = animatable.animatable
        val anim = animatable.animController.getPlayingAnim() ?: return false
        anim.apply {
            onEvent<AnimEvent.PhysicsTick> {
                if (skill.timeline.match(activeTime)) {
                    entity.grabManager.getGrabs().forEach {
                        AddTargetMovementComponent().attach(skill)
                        if (!skill.level.isClientSide) {
                            val pos = holder.getWorldBonePivot(bone, offset).toVec3()
                            it.setPos(pos)
                        }
                    }
                }
            }
        }
        return super.onAttach()
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<GrabComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.STRING.fieldOf("bone").forGetter { it.bone },
                Vec3.CODEC.fieldOf("offset").forGetter { it.offset },
                SkillTimeLine.Stamp.CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime }
            ).apply(it, ::GrabComponent)
        }
    }

}