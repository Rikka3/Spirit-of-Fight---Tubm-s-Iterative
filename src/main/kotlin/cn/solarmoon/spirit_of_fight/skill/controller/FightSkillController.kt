package cn.solarmoon.spirit_of_fight.skill.controller

import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.animation.preset_anim.PlayerStateAnimMachine
import cn.solarmoon.spark_core.animation.preset_anim.getStateMachine
import cn.solarmoon.spark_core.entity.preinput.getPreInput
import cn.solarmoon.spark_core.skill.controller.SkillController
import cn.solarmoon.spirit_of_fight.fighter.getPatch
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.LivingEntity
import org.ode4j.math.DVector3
import org.ode4j.ode.DBox
import ru.nsk.kstatemachine.statemachine.processEventBlocking

abstract class FightSkillController<T: LivingEntity>(
    name: String,
    override val holder: T,
    val animatable: IEntityAnimatable<out LivingEntity>,
    val baseAttackSpeed: Double,
    val twoHanded: Boolean
): SkillController<T>(name) {

    abstract val boxLength: DVector3
    abstract val boxOffset: DVector3

    private val commonComponents = mutableListOf<SkillControlComponent>()
    private val changeableComponents = mutableMapOf<String, SkillControlComponent>()

    val allComponents get() = changeableComponents.values.toMutableList().apply { addAll(commonComponents) }.toList()

    fun addComponent(component: SkillControlComponent) = component.apply { commonComponents.add(this) }

    override fun physTick() {
        super.physTick()
        // 攻击碰撞大小
        holder.getPatch().getMainAttackBody().let {
            val box = it.firstGeom as? DBox ?: return@let
            box.lengths = boxLength
            box.offsetPosition = boxOffset
        }
        holder.getPatch().getOffAttackBody().let {
            val box = it.firstGeom as? DBox ?: return@let
            box.lengths = boxLength
            box.offsetPosition = boxOffset
        }
        // 防守碰撞大小
        holder.getPatch().getMainGuardBody().let {
            val box = it.firstGeom as? DBox ?: return@let
            box.lengths = boxLength
            box.offsetPosition = boxOffset
        }
        holder.getPatch().getOffGuardBody().let {
            val box = it.firstGeom as? DBox ?: return@let
            box.lengths = boxLength
            box.offsetPosition = boxOffset
        }
    }

    override fun tick() {
        allComponents.forEach { it.tick(this) }
    }

    override fun onEntry() {
        super.onEntry()
        (holder as? LocalPlayer)?.getStateMachine()?.processEventBlocking(PlayerStateAnimMachine.ResetEvent)
    }

    override fun onExit() {
        super.onExit()
        holder.getPreInput().clear()
        if (allActiveSkills.isNotEmpty()) {
            animatable.animController.setAnimation(null, 2)
        }
        (holder as? LocalPlayer)?.getStateMachine()?.processEventBlocking(PlayerStateAnimMachine.ResetEvent)
    }

}