package cn.solarmoon.spirit_of_fight.skill.tree

import cn.solarmoon.spirit_of_fight.registry.common.SOFCapabilities
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

fun ItemStack.getSkillTrees(level: Level) = getCapability(SOFCapabilities.SKILL_TREE, level)

fun LivingEntity.getSkillTrees() = mainHandItem.getSkillTrees(level())