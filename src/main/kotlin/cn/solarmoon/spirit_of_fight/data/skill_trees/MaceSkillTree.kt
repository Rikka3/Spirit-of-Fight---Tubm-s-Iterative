package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.AerialDiveCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FallingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i

object MaceSkillTree {

    val MACE_COMBO = sofKey("mace.combo")
    val MACE_JUMP_ATTACK = sofKey("mace_jump_attack")
    val MACE_AERIAL_DIVE = sofKey("mace_aerial_dive")
    val MACE_SPRINT_ATTACK = sofKey("mace.sprint_attack")
    val MACE_BLOCK = sofKey("mace.block")
    val MACE_DODGE = sofKey("mace.dodge")
    val MACE_SPECIAL_ATTACK = sofKey("mace.special_attack")
    
    fun register(context: BootstrapContext<SkillTree>) {
        context.register(MACE_SPECIAL_ATTACK,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    cn.solarmoon.spirit_of_fight.skill.tree.node.FightSpiritConsumeNode(
                        listOf(
                            KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PRESS)),
                            cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition(100)
                        ),
                        SOFSkillTypes.MACE_SPECIAL_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                4
            )
        )
        context.register(MACE_COMBO,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.MACE_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.MACE_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.MACE_DEFAULT_COMBO_3,
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
        context.register(MACE_JUMP_ATTACK,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    CommonNode(
                        listOf(FallingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.PRESS))),
                        SOFSkillTypes.MACE_JUMP_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                2
            )
        )
        // Aerial dive attack - higher priority than jump attack (3 vs 2)
        // This will be checked first when falling with a target in range
        context.register(MACE_AERIAL_DIVE,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    CommonNode(
                        listOf(AerialDiveCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.PRESS))),
                        SOFSkillTypes.MACE_AERIAL_DIVE,
                        SOFPreInputs.ATTACK,
                        cooldown = 20
                    )
                ),
                3
            )
        )
        context.register(MACE_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.PRESS))),
                        SOFSkillTypes.MACE_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK,
                        cooldown = 60 // 3 seconds = 60 ticks
                    )
                ),
                1
            )
        )
        context.register(MACE_BLOCK,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.BLOCK.get().name to KeyEvent.PRESS))),
                        SOFSkillTypes.MACE_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf(SOFKeyMappings.BLOCK.get().name to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
        )
        context.register(MACE_DODGE,
            SkillTree(
                Ingredient.of(Items.MACE),
                listOf(
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.get().name to KeyEvent.PULSE)), OnGroundCondition()),
                        SOFSkillTypes.MACE_DODGE,
                        SOFPreInputs.DODGE
                    )
                )
            )
        )
    }
    
}
