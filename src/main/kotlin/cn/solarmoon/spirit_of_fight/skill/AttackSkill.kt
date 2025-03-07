package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.component.PhysicsBodyAttackComponent
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.SkillComponent
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spirit_of_fight.hit.EntityHitApplier
import cn.solarmoon.spirit_of_fight.hit.SOFHitTypes
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

open class AttackSkill(
    val animPlayer: PlayAnimationComponent,
    val attack: PhysicsBodyAttackComponent,
    val hitType: String = SOFHitTypes.LIGHT_STAB.toString().lowercase()
): Skill() {

    init {
        attack.doAttack = { attacker, target, o1, o2, manifoldId, attackSystem ->
            run {
                try {
                    if (attacker is Player) {
                        attacker.attack(target)
                        return@run true
                    } else if (attacker is LivingEntity) {
                        return@run target.hurt(attacker.damageSources().mobAttack(attacker), 1f)
                    }
                } catch (e: Exception) { }
                return@run false
            }
        }

        attack.preAttack = { attacker, target, o1, o2, manifoldId, attackSystem ->
            run {
                addTarget(target)
                target.hurtData?.blackBoard?.write(EntityHitApplier.HIT_TYPE, hitType)
            }
        }
    }

    override fun onActive(): Boolean {
        attack.attach(this)

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
        val CODEC: MapCodec<AttackSkill> = RecordCodecBuilder.mapCodec {
            it.group(
                PlayAnimationComponent.CODEC.fieldOf("animation").forGetter { it.animPlayer },
                PhysicsBodyAttackComponent.CODEC.fieldOf("physics_attack").forGetter { it.attack },
                Codec.STRING.optionalFieldOf("hit_type", SOFHitTypes.LIGHT_STAB.toString().lowercase()).forGetter { it.hitType }
            ).apply(it, ::AttackSkill)
        }
    }

}