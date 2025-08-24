package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.InputDirectionCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OffHandCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.WieldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.ConditionNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import org.joml.Vector2i

object SwordSkillTree {

    val SWORD_COMBO = sofKey("sword.combo")
    val SWORD_JUMP_ATTACK = sofKey("sword_jump_attack")
    val SWORD_SPRINT_ATTACK = sofKey("sword.sprint_attack")
    val SWORD_BLOCK = sofKey("sword.block")
    val SWORD_DODGE = sofKey("sword.dodge")
    val SWORD_SWITCH_ATTACK = sofKey("sword.switch_attack")
    
    fun register(context: BootstrapContext<SkillTree>) {
        context.register(SWORD_COMBO,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT)),
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.SWORD_DEFAULT_COMBO_1,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(OffHandCondition(Ingredient.of(Tags.Items.TOOLS_SHIELD)), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                                        SOFSkillTypes.SWORD_DEFAULT_SHIELD_COMBO_2,
                                        SOFPreInputs.ATTACK,
                                        children = listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.SWORD_DEFAULT_SHIELD_COMBO_3,
                                                SOFPreInputs.ATTACK
                                            )
                                        ),
                                        reserveTime = 5
                                    ),
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.SWORD_DEFAULT_COMBO_2,
                                        SOFPreInputs.ATTACK,
                                        listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.SWORD_DEFAULT_COMBO_3,
                                                SOFPreInputs.ATTACK
                                            )
                                        ),
                                        reserveTime = 5
                                    )
                                ),
                                reserveTime = 5
                            ),
                        )
                    ),
                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.SPECIAL)),
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.SWORD_SPECIAL_COMBO_1,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(OffHandCondition(Ingredient.of(Tags.Items.TOOLS_SHIELD)), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                                        SOFSkillTypes.SWORD_SPECIAL_SHIELD_COMBO_2,
                                        SOFPreInputs.ATTACK,
                                        children = listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.SWORD_SPECIAL_SHIELD_COMBO_3,
                                                SOFPreInputs.ATTACK
                                            )
                                        ),
                                        reserveTime = 5
                                    ),
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.SWORD_SPECIAL_COMBO_2,
                                        SOFPreInputs.ATTACK,
                                        listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.SWORD_SPECIAL_COMBO_3,
                                                SOFPreInputs.ATTACK
                                            )
                                        ),
                                        reserveTime = 5
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        context.register(SWORD_JUMP_ATTACK,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(JumpingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.SWORD_JUMP_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                2
            )
        )
        context.register(SWORD_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.SWORD_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    ),
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.SWORD_SPECIAL_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                1
            )
        )
        context.register(SWORD_BLOCK,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(OffHandCondition(Ingredient.of(Tags.Items.TOOLS_SHIELD)), InputDirectionCondition(MoveDirection.BACKWARD)),
                        SOFSkillTypes.SWORD_SHIELD_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(InputDirectionCondition(MoveDirection.BACKWARD))))
                        )
                    ),
                    CommonNode(
                        listOf(InputDirectionCondition(MoveDirection.BACKWARD)),
                        SOFSkillTypes.SWORD_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(InputDirectionCondition(MoveDirection.BACKWARD))))
                        )
                    )
                )
            )
        )
        context.register(SWORD_DODGE,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.get().name to KeyEvent.PULSE)), OnGroundCondition()),
                        SOFSkillTypes.SWORD_DODGE,
                        SOFPreInputs.DODGE
                    )
                )
            )
        )
        context.register(SWORD_SWITCH_ATTACK,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS))),
                        SOFSkillTypes.SWORD_SWITCH_ATTACK,
                        SOFPreInputs.SPECIAL_ATTACK,
                    )
                ),
                3
            )
        )
    }
    
}