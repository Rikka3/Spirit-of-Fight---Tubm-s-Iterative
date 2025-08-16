package cn.solarmoon.spirit_of_fight.data.skill_trees

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.data.SOFSkillTypes
import cn.solarmoon.spirit_of_fight.data.skill_trees.SOFSkillTrees.Companion.sofKey
import cn.solarmoon.spirit_of_fight.entity.WieldStyle
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.HitTargetCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OffHandCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.OnGroundCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.WieldCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import org.joml.Vector2i

object SwordSkillTree {

    val SWORD_DEFAULT_COMBO = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.combo")
    val SWORD_SPECIAL_COMBO = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.combo")
    val SWORD_JUMP_ATTACK = sofKey("sword_jump_attack")
    val SWORD_DEFAULT_SPRINT_ATTACK = sofKey("sword.${WieldStyle.DEFAULT.serializedName}.sprint_attack")
    val SWORD_SPECIAL_SPRINT_ATTACK = sofKey("sword.${WieldStyle.SPECIAL.serializedName}.sprint_attack")
    val SWORD_BLOCK = sofKey("sword.block")
    val SWORD_DODGE = sofKey("sword_dodge")
    val SWORD_SWITCH_ATTACK = sofKey("sword.switch_attack")
    
    fun register(context: BootstrapContext<SkillTree>) {
        context.register(SWORD_DEFAULT_COMBO,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.SWORD_DEFAULT_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.DEFAULT), OffHandCondition(Ingredient.of(Tags.Items.TOOLS_SHIELD)), KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.get().name to KeyEvent.PULSE))),
                                SOFSkillTypes.SHIELD_COMBO_C0,
                                SOFPreInputs.ATTACK,
                                children = listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3))), HitTargetCondition()),
                                        SOFSkillTypes.SWORD_SHIELD_COMBO_C1,
                                        SOFPreInputs.ATTACK,
                                        children = listOf(
                                            CommonNode(
                                                listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.SWORD_DEFAULT_COMBO_3,
                                                SOFPreInputs.ATTACK
                                            )
                                        )
                                    )
                                )
                            ),
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.SWORD_DEFAULT_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.SWORD_DEFAULT_COMBO_3,
                                        SOFPreInputs.ATTACK
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        context.register(SWORD_SPECIAL_COMBO,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.SWORD_SPECIAL_COMBO_1,
                        SOFPreInputs.ATTACK,
                        listOf(
                            CommonNode(
                                listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.SWORD_SPECIAL_COMBO_2,
                                SOFPreInputs.ATTACK,
                                listOf(
                                    CommonNode(
                                        listOf(WieldCondition(WieldStyle.SPECIAL), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.SWORD_SPECIAL_COMBO_3,
                                        SOFPreInputs.ATTACK
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
        context.register(SWORD_DEFAULT_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
                    CommonNode(
                        listOf(SprintingCondition(), WieldCondition(WieldStyle.DEFAULT), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.SWORD_DEFAULT_SPRINT_ATTACK,
                        SOFPreInputs.ATTACK
                    )
                ),
                1
            )
        )
        context.register(SWORD_SPECIAL_SPRINT_ATTACK,
            SkillTree(
                Ingredient.of(ItemTags.SWORDS),
                listOf(
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
                        listOf(OffHandCondition(Ingredient.of(Tags.Items.TOOLS_SHIELD)), KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS))),
                        SOFSkillTypes.SHIELD_GUARD,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS)))))
                        )
                    ),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS))),
                        SOFSkillTypes.SWORD_BLOCK,
                        SOFPreInputs.GUARD,
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf("key.back" to KeyEvent.PRESS)))))
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