package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.registry.common.SyncerTypes
import cn.solarmoon.spark_core.skill.SkillGroup
import cn.solarmoon.spark_core.skill.condition.HoldItemCondition
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.controller.ComboController
import cn.solarmoon.spirit_of_fight.skill.controller.PreInputCommonReleaseController
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.crafting.Ingredient

class SOFSkillGroups(
    private val builder: RegistrySetBuilder,
) {

    companion object {
        val SWORD = sofKey("sword")

        fun sofKey(id: String) = ResourceKey.create(SparkRegistries.SKILL_GROUP, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
        fun entityKey(type: EntityType<*>) = BuiltInRegistries.ENTITY_TYPE.getKey(type)
    }

    init {
        add(
            SWORD,
            SkillGroup(
                0,
                mapOf(SyncerTypes.ENTITY.get() to setOf(
                    entityKey(EntityType.PLAYER)
                )),
                listOf(
                    HoldItemCondition(Ingredient.of(ItemTags.SWORDS), InteractionHand.MAIN_HAND)
                ),
                listOf(
                    PreInputCommonReleaseController(),
                    ComboController(listOf(
                        SOFSkillTypes.SWORD_COMBO_0.location()
                    ))
                )
            )
        )
    }

    fun add(key: ResourceKey<SkillGroup>, type: SkillGroup) {
        builder.add(SparkRegistries.SKILL_GROUP) {
            it.register(key, type)
        }
    }



}