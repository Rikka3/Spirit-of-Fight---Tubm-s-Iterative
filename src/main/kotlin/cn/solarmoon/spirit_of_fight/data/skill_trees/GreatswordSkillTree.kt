package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.data.SOFItemTags
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i

object GreatswordSkillTree {

    val GREATSWORD_COMBO = sofKey("greatsword.combo")
    val GREATSWORD_JUMP_ATTACK = sofKey("greatsword_jump_attack")
    val GREATSWORD_SPRINT_ATTACK = sofKey("greatsword.sprint_attack")
    val GREATSWORD_BLOCK = sofKey("greatsword.block")
    val GREATSWORD_DODGE = sofKey("greatsword.dodge")
    val GREATSWORD_SPECIAL_ATTACK = sofKey("greatsword.special_attack")

    fun register(context: BootstrapContext<SkillTree>) {
        context.register(GREATSWORD_SPECIAL_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GREATSWORDS),
                listOf(
                    cn.solarmoon.spirit_of_fight.skill.tree.node.FightSpiritConsumeNode(
                        listOf(
                            KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS)),
                            cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition(100)
                        ),
                        SOFSkillTypes.GREATSWORD_SPECIAL_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                4
            )
        )
        context.register(GREATSWORD_COMBO,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GREATSWORDS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.GREATSWORD_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        children = listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.GREATSWORD_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                children = listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.GREATSWORD_DEFAULT_COMBO_3,
                                        SOFPreInputs.ATTACK,
                                        children = listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.GREATSWORD_DEFAULT_COMBO_4,
                                                SOFPreInputs.ATTACK
                                            )
                                        ),
                                        reserveTime = 5
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
        context.register(GREATSWORD_JUMP_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GREATSWORDS),
                listOf(
                    CommonNode(
                        listOf(JumpingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.GREATSWORD_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                2
            )
        )
        context.register(GREATSWORD_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GREATSWORDS),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.GREATSWORD_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                1
            )
        )
        context.register(GREATSWORD_BLOCK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GREATSWORDS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.BLOCK.get().name to KeyEvent.PRESS))),
                        SOFSkillTypes.GREATSWORD_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf(SOFKeyMappings.BLOCK.get().name to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
        )
        context.register(GREATSWORD_DODGE,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_GREATSWORDS),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.get().name to KeyEvent.PULSE)), OnGroundCondition()),
                        SOFSkillTypes.GREATSWORD_DODGE,
                        SOFPreInputs.DODGE
                    )
                )
            )
        )
    }

}
