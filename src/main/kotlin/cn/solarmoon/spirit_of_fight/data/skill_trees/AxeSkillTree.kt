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
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i

object AxeSkillTree {

    val AXE_COMBO = sofKey("axe.combo")
    val AXE_JUMP_ATTACK = sofKey("axe_jump_attack")
    val AXE_SPRINT_ATTACK = sofKey("axe.sprint_attack")
    val AXE_BLOCK = sofKey("axe.block")
    val AXE_DODGE = sofKey("axe.dodge")
    val AXE_SPECIAL_ATTACK = sofKey("axe.special_attack")

    fun register(context: BootstrapContext<SkillTree>) {
        context.register(AXE_SPECIAL_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_AXES),
                listOf(
                    cn.solarmoon.spirit_of_fight.skill.tree.node.FightSpiritConsumeNode(
                        listOf(
                            KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS)),
                            cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition(100)
                        ),
                        SOFSkillTypes.AXE_SPECIAL_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                4
            )
        )
        context.register(AXE_COMBO,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_AXES),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.AXE_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.AXE_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.AXE_DEFAULT_COMBO_3,
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
        context.register(AXE_JUMP_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_AXES),
                listOf(
                    CommonNode(
                        listOf(JumpingCondition(10), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.AXE_JUMP_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                2
            )
        )
        context.register(AXE_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_AXES),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.AXE_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                1
            )
        )
        context.register(AXE_BLOCK,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_AXES),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.BLOCK.get().name to KeyEvent.PRESS))),
                        SOFSkillTypes.AXE_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf(SOFKeyMappings.BLOCK.get().name to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
        )
        context.register(AXE_DODGE,
            SkillTree(
                Ingredient.of(SOFItemTags.FORGE_AXES),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.get().name to KeyEvent.PULSE)), OnGroundCondition()),
                        SOFSkillTypes.AXE_DODGE,
                        SOFPreInputs.DODGE
                    )
                )
            )
        )
    }

}
