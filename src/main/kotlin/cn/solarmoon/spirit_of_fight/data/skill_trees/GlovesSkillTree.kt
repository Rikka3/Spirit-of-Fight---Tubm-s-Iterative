package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OffHandCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.WieldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import org.joml.Vector2i

object GlovesSkillTree {

    val GLOVES_COMBO = sofKey("gloves.combo")
    val GLOVES_SPRINT_ATTACK = sofKey("gloves.sprint_attack")
    val GLOVES_BLOCK = sofKey("gloves.block")
    val GLOVES_DODGE = sofKey("gloves.dodge")
    val GLOVES_SWITCH_ATTACK = sofKey("gloves.switch_attack")

    fun register(context: BootstrapContext<SkillTree>) {
        context.register(GLOVES_COMBO,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.GLOVES_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.GLOVES_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.GLOVES_DEFAULT_COMBO_3,
                                        SOFPreInputs.ATTACK
                                    )
                                )
                            )
                        )
                    ),
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.GLOVES_SPECIAL_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.GLOVES_SPECIAL_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.GLOVES_SPECIAL_COMBO_3,
                                        SOFPreInputs.ATTACK
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        context.register(GLOVES_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.GLOVES_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    ),
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.GLOVES_SPECIAL_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                1
            )
        )
        context.register(GLOVES_BLOCK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    CommonNode(
                        listOf(OffHandCondition(Ingredient.of(Tags.Items.TOOLS_SHIELD)), KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS))),
                        SOFSkillTypes.SHIELD_GUARD,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS)))))
                        )
                    ),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS))),
                        SOFSkillTypes.GLOVES_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
        )
        context.register(GLOVES_DODGE,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.get().name to KeyEvent.PULSE)), OnGroundCondition()),
                        SOFSkillTypes.GLOVES_DODGE,
                        SOFPreInputs.DODGE
                    )
                )
            )
        )
        context.register(GLOVES_SWITCH_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS))),
                        SOFSkillTypes.GLOVES_SWITCH_ATTACK,
                        SOFPreInputs.SPECIAL_ATTACK,
                    )
                ),
                3
            )
        )
    }

}