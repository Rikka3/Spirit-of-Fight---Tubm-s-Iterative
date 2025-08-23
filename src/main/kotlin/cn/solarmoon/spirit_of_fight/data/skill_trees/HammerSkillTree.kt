package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.WieldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i

object HammerSkillTree {

    val HAMMER_COMBO = sofKey("hammer.combo")
    val HAMMER_JUMP_ATTACK = sofKey("hammer_jump_attack")
    val HAMMER_SPRINT_ATTACK = sofKey("hammer.sprint_attack")
    val HAMMER_BLOCK = sofKey("hammer.block")
    val HAMMER_DODGE = sofKey("hammer.dodge")
    val HAMMER_SWITCH_ATTACK = sofKey("hammer.switch_attack")

    fun register(context: BootstrapContext<SkillTree>) {
        context.register(HAMMER_COMBO,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_HAMMERS),
                listOf(
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.HAMMER_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.HAMMER_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.HAMMER_DEFAULT_COMBO_3,
                                        SOFPreInputs.ATTACK
                                    )
                                )
                            )
                        )
                    ),
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.HAMMER_SPECIAL_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.HAMMER_SPECIAL_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.HAMMER_SPECIAL_COMBO_3,
                                        SOFPreInputs.ATTACK
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        context.register(HAMMER_JUMP_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_HAMMERS),
                listOf(
                    CommonNode(
                        listOf(JumpingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.HAMMER_JUMP_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                2
            )
        )
        context.register(HAMMER_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_HAMMERS),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.HAMMER_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    ),
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.HAMMER_SPECIAL_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                1
            )
        )
        context.register(HAMMER_BLOCK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_HAMMERS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS))),
                        SOFSkillTypes.HAMMER_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
        )
        context.register(HAMMER_DODGE,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_HAMMERS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.get().name to KeyEvent.PULSE)), OnGroundCondition()),
                        SOFSkillTypes.HAMMER_DODGE,
                        SOFPreInputs.DODGE
                    )
                )
            )
        )
        context.register(HAMMER_SWITCH_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_HAMMERS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS))),
                        SOFSkillTypes.HAMMER_SWITCH_ATTACK,
                        SOFPreInputs.SPECIAL_ATTACK,
                    )
                ),
                3
            )
        )
    }

}