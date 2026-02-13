package cn.solarmoon.spirit_of_fight.data.skills

import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import org.joml.Vector3f

object VindicatorSkill {

    val SWIPE_ATTACK = sofKey("vindicator_swipe_attack")
    val COUNTER_ATTACK = sofKey("vindicator_counter_attack")
    val SPRINT_ATTACK = sofKey("vindicator_sprint_attack")
    val ALL = listOf(SWIPE_ATTACK, COUNTER_ATTACK)

    fun random() = ALL.random()

    fun sofKey(id: String) = ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id)
    fun entityKey(type: EntityType<*>) = BuiltInRegistries.ENTITY_TYPE.getKey(type)


    fun register(register: BootstrapContext<SkillType<*>>) {
        val axeBoxSize = Vector3f(0.75f, 0.75f, 0.75f)
        val axeBoxOffset = Vector3f(0.0f, 0.0f, -0.55f)
    }

}