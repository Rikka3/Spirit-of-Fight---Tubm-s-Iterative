package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.component.AnimBoxAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.sub.box.BoxFollowAnimatedBoneSubcomponent
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec2
import org.joml.Vector3f

class SOFSkillTypes(
    private val builder: RegistrySetBuilder
) {

    companion object {
        val SWORD_COMBO_0 = sofKey("sword_combo_0")

        fun sofKey(id: String) = ResourceKey.create(SparkRegistries.SKILL_TYPE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
    }

    init {
        add(SWORD_COMBO_0, SkillType(listOf(
            AnimBoxAttackComponent(
                AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_0"),
                BoxFollowAnimatedBoneSubcomponent("rightItem", Vector3f(0.75f, 0.75f, 1.25f), Vector3f(0f, 0f, -1.25f/2), listOf(Vec2(0.8f, 1.1f)))
            )
        )))
    }

    fun add(key: ResourceKey<SkillType>, type: SkillType) {
        builder.add(SparkRegistries.SKILL_TYPE) {
            it.register(key, type)
        }
    }

}