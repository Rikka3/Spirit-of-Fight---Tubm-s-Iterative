package cn.solarmoon.spirit_of_fight.skill

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.play.AnimEvent
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.addRelativeMovement
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillEvent
import cn.solarmoon.spark_core.skill.SkillPhase
import cn.solarmoon.spark_core.skill.payload.SkillPayload
import cn.solarmoon.spirit_of_fight.poise_system.EntityHitApplier
import cn.solarmoon.spirit_of_fight.spirit.getFightSpirit
import cn.solarmoon.spirit_of_fight.util.SkillHelper
import com.jme3.bullet.collision.PhysicsCollisionObject
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.network.PacketDistributor

class BlockSkill: Skill() {
    class BlockHurt(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()
    class PrecisionBlock(val event: LivingIncomingDamageEvent, val hitPos: Vec3): SkillEvent()

    lateinit var guardAnim: AnimInstance
    lateinit var hurtAnim: AnimInstance
    lateinit var guardBody: PhysicsCollisionObject
    var blockReady = false

    init {
        init {
            val animatable = holder as? IEntityAnimatable<*> ?: return@init
            val entity = animatable.animatable
            val animPath = config.readNonNull<String>("anim_path")
            guardAnim = animatable.createAnimation("spirit_of_fight:spirit_of_fight/animations/player/fight_skill/fightskill_sword", animPath)
            hurtAnim = animatable.createAnimation("spirit_of_fight:spirit_of_fight/animations/player/fight_skill/fightskill_sword", "$animPath.hurt").apply {
                EntityHitApplier.hitAnimDoFreeze(this)
            }
            val testAnim = animatable.createAnimation("spirit_of_fight:spirit_of_fight/animations/player/fight_skill/fightskill_sword", "hit.landing").apply {
                EntityHitApplier.hitAnimDoFreeze(this)
            }
            val consume = config.read("spirit_consumption", 25.0).toInt()

            onEvent<SkillEvent.ActiveStart> {
                guardBody = config.readNonNull("block_body")
                animatable.animController.setAnimation(guardAnim, 5)
            }

            hurtAnim.onEvent<AnimEvent.Completed> {
                animatable.animController.setAnimation(guardAnim, 0)
            }

            val hurtMovement = config.read("hurt_movement", Vec3(0.0, 0.0, 1.0))

            onEvent<SkillEvent.Hurt> {
                val event = it.event
                val sourcePos = event.source.sourcePosition ?: return@onEvent
                SkillHelper.guard(it.event.source, guardBody) { body, hitPos ->
                    val attacker = event.source.entity
                    if (blockReady) {
                        triggerEvent(PrecisionBlock(event, hitPos))
                        it.event.isCanceled = true
                    } else if (hurtAnim.isCancelled) {
                        val fs = entity.getFightSpirit()
                        fs.removeStage(consume)
                        fs.syncToClient(entity.id)
                        if (!fs.isEmpty) {
                            hurtAnim.refresh()
                            animatable.animController.setAnimation(hurtAnim, 0)
                            entity.addRelativeMovement(sourcePos, hurtMovement)
                            PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag().apply {
                                putBoolean("hurt", true)
                                putDouble("x", sourcePos.x)
                                putDouble("y", sourcePos.y)
                                putDouble("z", sourcePos.z)
                            }))
                            triggerEvent(BlockHurt(event, hitPos))
                            it.event.isCanceled = true
                        } else {
                            animatable.animController.setAnimation(testAnim, 0)
                            PacketDistributor.sendToAllPlayers(SkillPayload(this, CompoundTag()))
                        }
                    }
                }
            }

            sync { data, _ ->
                if (data.getBoolean("hurt")) {
                    hurtAnim.refresh()
                    animatable.animController.setAnimation(hurtAnim, 0)
                    entity.addRelativeMovement(Vec3(data.getDouble("x"), data.getDouble("y"), data.getDouble("z")), hurtMovement)
                } else {
                    animatable.animController.setAnimation(testAnim, 0)
                }
            }

            onEvent<SkillEvent.End> {
                guardAnim.cancel()
                entity.removeBody(guardBody.name)
            }

            onEvent<SkillEvent.Active> {
                blockReady = false
                if (canTransitionTo(SkillPhase.END) && guardAnim.isCancelled) end()
            }

            canTransitionTo = { p ->
                if (p == SkillPhase.END) {
                    hurtAnim.isCancelled
                } else true
            }
        }
    }

    fun readyPrecisionBlock() {
        blockReady = true
    }

}