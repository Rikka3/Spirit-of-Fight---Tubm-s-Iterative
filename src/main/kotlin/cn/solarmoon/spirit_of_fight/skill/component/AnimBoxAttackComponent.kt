package cn.solarmoon.spirit_of_fight.skill.component

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.animation.anim.origin.OAnimationSet
import cn.solarmoon.spark_core.animation.anim.play.AnimInstance
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.physics.host.PhysicsHost
import cn.solarmoon.spark_core.skill.SkillComponent
import cn.solarmoon.spark_core.skill.SkillInstance
import cn.solarmoon.spirit_of_fight.phys.attack.CommonAttackContactListener
import cn.solarmoon.spirit_of_fight.skill.component.sub.box.BoxGenerationSubcomponent
import cn.solarmoon.spirit_of_fight.skill.component.sub.preinput.PreInputReleaseSubcomponent
import cn.solarmoon.spirit_of_fight.skill.component.sub.preinput.PreInputSubcomponent
import com.jme3.bullet.collision.PhysicsCollisionObject
import com.jme3.bullet.objects.PhysicsRigidBody
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.Entity
import kotlin.properties.Delegates

class AnimBoxAttackComponent(
    val animResource: AnimIndex,
    val boxType: BoxGenerationSubcomponent,
    val preInputComponents: List<PreInputSubcomponent>
): SkillComponent {

    override val codec: MapCodec<out SkillComponent> = CODEC

    override fun copy(): SkillComponent {
        return AnimBoxAttackComponent(animResource, boxType, preInputComponents)
    }

    var collideEnableCheck by Delegates.observable(false) { _, old, new ->
        if (old != new) {
            if (new) onBoxEntry()
            else onBoxExit()
        }
    }

    lateinit var body: PhysicsRigidBody
    lateinit var anim: AnimInstance

    fun onBoxEntry() {
        body.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01)
    }

    fun onBoxExit() {
        body.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01)
    }

    override fun onActive(skill: SkillInstance): Boolean {
        val animatable = skill.holder as? IEntityAnimatable<*> ?: return false
        val entity = animatable.animatable
        body = boxType.createBody(entity).apply { addContactListener(CommonAttackContactListener()) }
        anim = AnimInstance.create(animatable, animResource.name, OAnimationSet.get(animResource.index).getAnimation(animResource.name)!!) {
            shouldTurnBody = true
            boxType.activeTime.forEachIndexed { index, range ->
                onPhysTick {
                    collideEnableCheck = time in range.x.toDouble()..range.y.toDouble()
                }
            }

            onEnd {
                skill.end()
            }
        }
        animatable.animController.setAnimation(anim, 7)
        return true
    }

    override fun onUpdate(skill: SkillInstance): Boolean {
        val entity = skill.holder as? Entity ?: return true
        preInputComponents.forEach {
            if (it is PreInputReleaseSubcomponent) it.release(entity.getPreInput(), anim.time)
        }
        return true
    }

    override fun onStop(skill: SkillInstance): Boolean {
        val animatable = skill.holder as? IEntityAnimatable<*> ?: return false
        val entity = animatable.animatable
        anim.cancel()
        body.name.let { (skill.holder as? PhysicsHost)?.removeBody(it) }
        return true
    }

    companion object {
        val CODEC: MapCodec<AnimBoxAttackComponent> = RecordCodecBuilder.mapCodec {
            it.group(
                AnimIndex.CODEC.fieldOf("anim_location").forGetter { it.animResource },
                BoxGenerationSubcomponent.CODEC.fieldOf("box").forGetter { it.boxType },
                PreInputSubcomponent.CODEC.listOf().optionalFieldOf("preinput_controller", listOf()).forGetter { it.preInputComponents }
            ).apply(it) { a, b, p -> AnimBoxAttackComponent(a, b as BoxGenerationSubcomponent, p as List<PreInputSubcomponent>) }
        }
    }

}