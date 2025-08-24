package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.util.MoveDirection
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.AnyCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.InputDirectionCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.WieldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.ConditionNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i
import java.util.concurrent.ConcurrentLinkedQueue

object GlovesSkillTree {

    val GLOVES_COMBO = sofKey("gloves.combo")
    val GLOVES_ATTACK = sofKey("gloves.attack")
    val GLOVES_BLOCK = sofKey("gloves.block")
    val GLOVES_DODGE = sofKey("gloves.dodge")
    val GLOVES_SWITCH_ATTACK = sofKey("gloves.switch_attack")

    fun register(context: BootstrapContext<SkillTree>) {
        context.register(GLOVES_COMBO,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT)),
                        listOf(
                            CommonNode(
                                listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                                SOFSkillTypes.GLOVES_DEFAULT_SPRINT_ATTACK,
                                SOFPreInputs.ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.GLOVES_DEFAULT_COMBO_1,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.GLOVES_DEFAULT_COMBO_2,
                                        SOFPreInputs.ATTACK,
                                        listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.GLOVES_DEFAULT_COMBO_3,
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
                                listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                                SOFSkillTypes.GLOVES_SPECIAL_SPRINT_ATTACK,
                                SOFPreInputs.ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.GLOVES_SPECIAL_COMBO_1,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.GLOVES_SPECIAL_COMBO_2,
                                        SOFPreInputs.ATTACK,
                                        listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.GLOVES_SPECIAL_COMBO_3,
                                                SOFPreInputs.ATTACK
                                            )
                                        ),
                                        reserveTime = 5
                                    )
                                ),
                                reserveTime = 5
                            )
                        )
                    )
                )
            )
        )
        context.register(GLOVES_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GLOVES),
                listOf(
                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT)),
                        listOf(
                            CommonNode(
                                listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                                SOFSkillTypes.GLOVES_DEFAULT_SPRINT_ATTACK,
                                SOFPreInputs.ATTACK
                            ),
                        )
                    ),
                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.SPECIAL)),
                        listOf(
                            CommonNode(
                                listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                                SOFSkillTypes.GLOVES_SPECIAL_SPRINT_ATTACK,
                                SOFPreInputs.ATTACK
                            )
                        )
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
                        listOf(InputDirectionCondition(MoveDirection.BACKWARD)),
                        SOFSkillTypes.GLOVES_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(InputDirectionCondition(MoveDirection.BACKWARD))))
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
                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT)),
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), AnyCondition(ConcurrentLinkedQueue(listOf(InputDirectionCondition(MoveDirection.FORWARD), InputDirectionCondition(null))))),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_FORWARD,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), InputDirectionCondition(MoveDirection.LEFT)),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_LEFT,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), InputDirectionCondition(MoveDirection.RIGHT)),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_RIGHT,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), InputDirectionCondition(MoveDirection.BACKWARD)),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_BACKWARD,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                        )
                    ),

                    ConditionNode(
                        listOf(WieldCondition(WieldStyle.SPECIAL)),
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), AnyCondition(ConcurrentLinkedQueue(listOf(InputDirectionCondition(MoveDirection.FORWARD), InputDirectionCondition(null))))),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_FORWARD,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), InputDirectionCondition(MoveDirection.LEFT)),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_LEFT,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), InputDirectionCondition(MoveDirection.RIGHT)),
                                SOFSkillTypes.GLOVES_SPECIAL_SWITCH_ATTACK_RIGHT,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS_ONCE)), InputDirectionCondition(MoveDirection.BACKWARD)),
                                SOFSkillTypes.GLOVES_DEFAULT_SWITCH_ATTACK_BACKWARD,
                                SOFPreInputs.SPECIAL_ATTACK
                            ),
                        )
                    ),
                ),
                2
            )
        )
    }

}