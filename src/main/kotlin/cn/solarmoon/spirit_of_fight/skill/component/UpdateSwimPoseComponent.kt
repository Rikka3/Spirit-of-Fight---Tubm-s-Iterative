package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.skill.component.SkillComponent
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Pose

class UpdateSwimPoseComponent: SkillComponent() {

    override fun onTick() {
        val entity = skill.holder as? Entity ?: return
        entity.pose = Pose.SWIMMING
    }

    override val codec: MapCodec<out SkillComponent> = CODEC

    companion object {
        val CODEC: MapCodec<UpdateSwimPoseComponent> = RecordCodecBuilder.mapCodec {
            it.group(Codec.EMPTY.forGetter { Unit.INSTANCE }).apply(it) { UpdateSwimPoseComponent() }
        }
    }

}