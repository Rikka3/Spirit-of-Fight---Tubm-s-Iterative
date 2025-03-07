package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillTimeLine
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import com.mojang.blaze3d.platform.GlConst.GL_LEQUAL
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.inventory.PlayerEnderChestContainer
import net.minecraft.world.phys.Vec2
import net.neoforged.bus.api.Event
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

open class DodgeSkill(
    val animPlayer: PlayAnimationComponent,
    val invincibleTime: List<SkillTimeLine.Stamp> = listOf(),
    val perfectDodgeTime: List<SkillTimeLine.Stamp> = listOf(),
    val perfectDodgeOnlyOnce: Boolean = true,
    val onAnimLifeCycle: List<SkillComponent> = listOf(),
    val onPerfectDodge: List<SkillComponent> = listOf(),
    val enablePerfectDodge: Boolean = true,
): Skill() {

    private var perfectDodgeCheck = false

    override fun onHurt(event: LivingIncomingDamageEvent) {
        val victim = event.entity
        if (victim == holder) {
            if (timeline.match(invincibleTime)) {
                if (!perfectDodgeCheck && enablePerfectDodge && timeline.match(perfectDodgeTime)) {
                    onPerfectDodge(event)
                    if (perfectDodgeOnlyOnce) perfectDodgeCheck = true
                }
                event.isCanceled = true
            }
        }
    }

    open fun onPerfectDodge(event: LivingIncomingDamageEvent) {
        onPerfectDodge.forEach { it.attach(this) }
        PacketDistributor.sendToAllPlayers(SkillPayload(this))
    }

    override fun onSync(data: CompoundTag, context: IPayloadContext) {
        onPerfectDodge.forEach { it.attach(this) }
    }

    override fun onActive(): Boolean {
        val animatable = holder as? IEntityAnimatable<*> ?: return false
        val entity = animatable.animatable

        perfectDodgeCheck = false

        val direction = entity.moveDirection ?: return false

        animPlayer.animIndex = AnimIndex(animPlayer.animIndex.index, animPlayer.animIndex.name + "_${direction.toString().lowercase()}")
        animPlayer.attach(this)
        animPlayer.anim.apply {
            shouldTurnBody = true
            onEvent<AnimEvent.End> {
                end()
            }
        }
        return true
    }

    override fun onEnd() {
        animPlayer.anim.cancel()
    }

    override val codec: MapCodec<out Skill> = CODEC

    companion object {
        val CODEC: MapCodec<DodgeSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                PlayAnimationComponent.CODEC.fieldOf("animation").forGetter { it.animPlayer },
                SkillTimeLine.Stamp.CODEC.listOf().optionalFieldOf("invincible_time", listOf()).forGetter { it.invincibleTime },
                SkillTimeLine.Stamp.CODEC.listOf().optionalFieldOf("perfect_dodge_time", listOf()).forGetter { it.perfectDodgeTime },
                Codec.BOOL.optionalFieldOf("only_one_perfect_dodge_allowed", true).forGetter { it.perfectDodgeOnlyOnce },
                SkillComponent.CODEC.listOf().fieldOf("on_anim_life_cycle").forGetter { it.onAnimLifeCycle },
                SkillComponent.CODEC.listOf().fieldOf("on_perfect_dodge").forGetter { it.onPerfectDodge },
                Codec.BOOL.optionalFieldOf("enable_perfect_dodge", true).forGetter { it.enablePerfectDodge },
            ).apply(it, ::DodgeSkill)
        }
    }

}