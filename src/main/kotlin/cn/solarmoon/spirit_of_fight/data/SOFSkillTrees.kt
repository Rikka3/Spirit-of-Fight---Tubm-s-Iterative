package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.client.SOFKeyMappings
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.AnyCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.FightSpiritCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.CommonNode
import cn.solarmoon.spirit_of_fight.skill.tree.condition.HitTargetCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.JumpingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.KeyInputCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.ReverseCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillEndCondition
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SprintingCondition
import cn.solarmoon.spirit_of_fight.skill.tree.node.FightSpiritConsumeNode
import cn.solarmoon.spirit_of_fight.skill.tree.node.StopNode
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.crafting.Ingredient
import org.joml.Vector2i
import java.util.concurrent.ConcurrentLinkedQueue

class SOFSkillTrees(
    builder: RegistrySetBuilder,
) {

    companion object {
        val SWORD_COMBO = sofKey("sword_combo")
        val SWORD_JUMP_ATTACK = sofKey("sword_jump_attack")
        val SWORD_SPRINT_ATTACK = sofKey("sword_sprint_attack")
        val SWORD_GUARD = sofKey("sword_guard")
        val SWORD_DODGE = sofKey("sword_dodge")
        val SWORD_SKILL = sofKey("sword_skill")

        val HAMMER_COMBO = sofKey("hammer_combo")
        val HAMMER_JUMP_ATTACK = sofKey("hammer_jump_attack")
        val HAMMER_SPRINT_ATTACK = sofKey("hammer_sprint_attack")
        val HAMMER_GUARD = sofKey("hammer_guard")
        val HAMMER_DODGE = sofKey("hammer_dodge")
        val HAMMER_SKILL = sofKey("hammer_skill")

        fun sofKey(id: String) = ResourceKey.create(SOFRegistries.SKILL_TREE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
    }

    init {
        builder.add(SOFRegistries.SKILL_TREE) {
            it.register(SWORD_COMBO,
                SkillTree(
                    Ingredient.of(ItemTags.SWORDS),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.SWORD_COMBO_0.location(),
                        listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.SWORD_COMBO_1.location(),
                                listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.SWORD_COMBO_2.location()
                                    )
                                ),
                                10
                            )
                        ),
                        10
                    )
                )
            )
            it.register(SWORD_JUMP_ATTACK,
                SkillTree(
                    Ingredient.of(ItemTags.SWORDS),
                    CommonNode(
                        listOf(JumpingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.SWORD_JUMP_ATTACK.location()
                    ),
                    2
                )
            )
            it.register(SWORD_SPRINT_ATTACK,
                SkillTree(
                    Ingredient.of(ItemTags.SWORDS),
                    CommonNode(
                        listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.SWORD_SPRINTING_ATTACK.location()
                    ),
                    1
                )
            )
            it.register(SWORD_GUARD,
                SkillTree(
                    Ingredient.of(ItemTags.SWORDS),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.GUARD.name to KeyEvent.PRESS))),
                        SOFSkillTypes.SWORD_GUARD.location(),
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf(SOFKeyMappings.GUARD.name to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
            it.register(SWORD_DODGE,
                SkillTree(
                    Ingredient.of(ItemTags.SWORDS),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.name to KeyEvent.PRESS_ONCE))),
                        SOFSkillTypes.SWORD_DODGE.location()
                    )
                )
            )
            it.register(SWORD_SKILL,
                SkillTree(
                    Ingredient.of(ItemTags.SWORDS),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.name to KeyEvent.PRESS_ONCE))),
                        SOFSkillTypes.SWORD_SUPER_ARMOR_BLOCK.location(),
                        children = listOf(
                            FightSpiritConsumeNode(
                                listOf(
                                    AnyCondition(ConcurrentLinkedQueue(
                                        listOf(
                                            KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.name to KeyEvent.RELEASE)),
                                            SkillEndCondition())
                                        )
                                    ),
                                    FightSpiritCondition(100)
                                ),
                                SOFSkillTypes.SWORD_SPECIAL_ATTACK.location(),
                                preInputId = "special_attack_release"
                            )
                        ),
                        1
                    )
                )
            )

            it.register(HAMMER_COMBO,
                SkillTree(
                    Ingredient.of(SOFItemTags.FORGE_HAMMER),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                        SOFSkillTypes.HAMMER_COMBO_0.location(),
                        children = listOf(
                            CommonNode(
                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                SOFSkillTypes.HAMMER_COMBO_1.location(),
                                children = listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                        SOFSkillTypes.HAMMER_COMBO_2.location()
                                    )
                                ),
                                10
                            ),
                            CommonNode(
                                listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.name to KeyEvent.PRESS_ONCE))),
                                SOFSkillTypes.HAMMER_COMBO_C0.location(),
                                children = listOf(
                                    CommonNode(
                                        listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3))), HitTargetCondition()),
                                        SOFSkillTypes.HAMMER_COMBO_C1.location(),
                                        children = listOf(
                                            CommonNode(
                                                listOf(KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE), listOf(Vector2i(0, 3)))),
                                                SOFSkillTypes.HAMMER_COMBO_2.location()
                                            )
                                        ),
                                        10
                                    )
                                ),
                                10
                            ),
                        ),
                        10
                    )
                )
            )
            it.register(HAMMER_JUMP_ATTACK,
                SkillTree(
                    Ingredient.of(SOFItemTags.FORGE_HAMMER),
                    CommonNode(
                        listOf(JumpingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.HAMMER_JUMP_ATTACK.location()
                    ),
                    2
                )
            )
            it.register(HAMMER_SPRINT_ATTACK,
                SkillTree(
                    Ingredient.of(SOFItemTags.FORGE_HAMMER),
                    CommonNode(
                        listOf(SprintingCondition(), KeyInputCondition(mapOf("key.attack" to KeyEvent.RELEASE))),
                        SOFSkillTypes.HAMMER_SPRINTING_ATTACK.location()
                    ),
                    1
                )
            )
            it.register(HAMMER_GUARD,
                SkillTree(
                    Ingredient.of(SOFItemTags.FORGE_HAMMER),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.GUARD.name to KeyEvent.PRESS))),
                        SOFSkillTypes.HAMMER_GUARD.location(),
                        children = listOf(
                            StopNode(listOf(ReverseCondition(KeyInputCondition(mapOf(SOFKeyMappings.GUARD.name to KeyEvent.PRESS)))))
                        )
                    )
                )
            )
            it.register(HAMMER_DODGE,
                SkillTree(
                    Ingredient.of(SOFItemTags.FORGE_HAMMER),
                    CommonNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.DODGE.name to KeyEvent.PRESS_ONCE))),
                        SOFSkillTypes.HAMMER_DODGE.location()
                    )
                )
            )
            it.register(HAMMER_SKILL,
                SkillTree(
                    Ingredient.of(SOFItemTags.FORGE_HAMMER),
                    FightSpiritConsumeNode(
                        listOf(KeyInputCondition(mapOf(SOFKeyMappings.SPECIAL_ATTACK.name to KeyEvent.PRESS_ONCE)), FightSpiritCondition(100)),
                        SOFSkillTypes.HAMMER_SPECIAL_ATTACK.location(),
                        preInputId = "skill"
                    ),
                    -1
                )
            )


        }
    }

}