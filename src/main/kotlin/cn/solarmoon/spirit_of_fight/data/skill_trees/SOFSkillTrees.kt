package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

class SOFSkillTrees(
    builder: RegistrySetBuilder,
) {

    companion object {
        fun sofKey(id: String) = ResourceKey.create(SOFRegistries.SKILL_TREE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
    }

    init {
        builder.add(SOFRegistries.SKILL_TREE) {
            SwordSkillTree.register(it)
            SpearSkillTree.register(it)
            GlovesSkillTree.register(it)
        }
    }

}