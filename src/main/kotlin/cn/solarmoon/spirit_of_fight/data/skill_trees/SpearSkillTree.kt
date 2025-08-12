package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.WieldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i

object SpearSkillTree {

    val SPEAR_DEFAULT_COMBO = sofKey("spear.${WieldStyle.DEFAULT.serializedName}.combo")

    fun register(context: BootstrapContext<SkillTree>) {
        context.register(SPEAR_DEFAULT_COMBO,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_SPEARS),
                listOf(
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.SPEAR_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.SPEAR_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.SPEAR_DEFAULT_COMBO_3,
                                        SOFPreInputs.ATTACK
                                    )
                                ),
                                10
                            )
                        ),
                        10
                    )
                )
            )
        )
    }

}