package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spark_core.skill.node.BehaviorTree
import cn.solarmoon.spark_core.skill.node.bases.ParallelNode
import cn.solarmoon.spark_core.skill.node.bases.SequenceNode
import cn.solarmoon.spark_core.skill.node.leaves.AnimSpeedModifierComponent
import cn.solarmoon.spark_core.skill.node.leaves.AttackDamageModifierComponent
import cn.solarmoon.spark_core.skill.node.leaves.CameraShakeComponent
import cn.solarmoon.spark_core.skill.node.leaves.EmptyNode
import cn.solarmoon.spark_core.skill.node.leaves.EndChildrenNode
import cn.solarmoon.spark_core.skill.node.leaves.EndSkillNode
import cn.solarmoon.spark_core.skill.node.leaves.InvincibilityComponent
import cn.solarmoon.spark_core.skill.node.leaves.KnockBackNode
import cn.solarmoon.spark_core.skill.node.leaves.MoveSetComponent
import cn.solarmoon.spark_core.skill.node.leaves.PlayAnimationNode
import cn.solarmoon.spark_core.skill.node.leaves.PreInputReleaseComponent
import cn.solarmoon.spark_core.skill.node.leaves.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.node.leaves.PreventYRotComponent
import cn.solarmoon.spark_core.skill.node.leaves.RefreshChildrenNode
import cn.solarmoon.spark_core.skill.node.leaves.SummonShadowComponent
import cn.solarmoon.spark_core.skill.node.leaves.collision.BoxFollowAnimatedBoneComponent
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.component.AddFightSpiritComponent
import cn.solarmoon.spirit_of_fight.skill.component.CommonAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.GuardComponent
import cn.solarmoon.spirit_of_fight.skill.component.PerfectDodgeComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

class SOFSkillTypes(
    builder: RegistrySetBuilder
) {

    companion object {
        val SWORD_COMBO_0 = sofKey("sword_combo_0")
        val SWORD_COMBO_1 = sofKey("sword_combo_1")
        val SWORD_COMBO_2 = sofKey("sword_combo_2")
        val SWORD_DODGE = sofKey("sword_dodge")
        val SWORD_GUARD = sofKey("sword_guard")

        fun sofKey(id: String) = ResourceKey.create(SparkRegistries.SKILL_TYPE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
    }

    init {
        val swordBoxSize = Vector3f(0.75f, 0.75f, 1.35f)
        val swordBoxOffset = Vector3f(0f, 0f, -1.35f/2)
        builder.add(SparkRegistries.SKILL_TYPE) {
            it.register(SWORD_COMBO_0,
                SkillType(
                    BehaviorTree(
                        PlayAnimationNode(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_0"), 2, true,
                            onAnimTick = ParallelNode(listOf(
                                BoxFollowAnimatedBoneComponent("attack", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.3f, 0.45f))),
                                CommonAttackComponent(
                                    preFirstAttackComponents = ParallelNode(listOf(
                                        AnimSpeedModifierComponent(),
                                        CameraShakeComponent(2, 1f, 2f)
                                    )),
                                    preAttackComponents = ParallelNode(listOf(
                                        AddFightSpiritComponent(40)
                                    ))
                                ),
                                AttackDamageModifierComponent(),
                                MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2)))),
                                PreInputReleaseComponent(listOf(Vec2(0.40f, Float.MAX_VALUE))),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(listOf(Vec2(0.3f, Float.MAX_VALUE))),
                                PreventMoveWithBackComponent()
                            )),
                            onAnimEnd = EndSkillNode()
                        )
                    )
                )
            )
            it.register(SWORD_COMBO_1,
                SkillType(
                    BehaviorTree(
                        PlayAnimationNode(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_1"), 2, true,
                            onAnimTick = ParallelNode(listOf(
                                BoxFollowAnimatedBoneComponent("attack", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.40f))),
                                CommonAttackComponent(
                                    preFirstAttackComponents = ParallelNode(listOf(
                                        AnimSpeedModifierComponent(),
                                        CameraShakeComponent(2, 1f, 2f)
                                    )),
                                    preAttackComponents = ParallelNode(listOf(
                                        AddFightSpiritComponent(40)
                                    ))
                                ),
                                AttackDamageModifierComponent(),
                                MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2)))),
                                PreInputReleaseComponent(listOf(Vec2(0.40f, Float.MAX_VALUE))),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(listOf(Vec2(0.25f, Float.MAX_VALUE))),
                                PreventMoveWithBackComponent()
                            )),
                            onAnimEnd = EndSkillNode()
                        )
                    )
                )
            )
            it.register(SWORD_COMBO_2,
                SkillType(
                    BehaviorTree(
                        PlayAnimationNode(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_2"), 2, true,
                            onAnimTick = ParallelNode(listOf(
                                BoxFollowAnimatedBoneComponent("attack", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.5f))),
                                CommonAttackComponent(
                                    preFirstAttackComponents = ParallelNode(listOf(
                                        AnimSpeedModifierComponent(),
                                        CameraShakeComponent(3, 1.5f, 3f)
                                    )),
                                    preAttackComponents = ParallelNode(listOf(
                                        AddFightSpiritComponent(60)
                                    ))
                                ),
                                AttackDamageModifierComponent(1.25f),
                                MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.3f), Vec3(0.0, 0.0, 0.33)))),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(listOf(Vec2(0.25f, Float.MAX_VALUE))),
                                PreventMoveWithBackComponent()
                            )),
                            onAnimEnd = EndSkillNode()
                        )
                    )
                )
            )
            it.register(SWORD_DODGE,
                SkillType(
                    BehaviorTree(
                        PlayAnimationNode(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:dodge_forward"), 2, true,
                            onAnimTick = ParallelNode(listOf(
                                InvincibilityComponent(
                                    onImmuneToDamage = PerfectDodgeComponent(true, listOf(Vec2(0f, 0.2f)),
                                        onPerfectDodge = SummonShadowComponent()
                                    )
                                ),
                                MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.1f), Vec3(0.0, 0.0, 1.0))), true),
                                PreInputReleaseComponent(listOf(Vec2(0.35f, Float.MAX_VALUE)), Either.right(setOf("dodge", "move"))),
                                PreventLocalInputComponent(),
                                PreventYRotComponent()
                            )),
                            onAnimEnd = EndSkillNode()
                        )
                    )
                )
            )
            it.register(SWORD_GUARD,
                SkillType(
                    BehaviorTree(
                        ParallelNode(listOf(
                            PlayAnimationNode(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:guard"), 10, true,
                                onAnimTick = ParallelNode(listOf(
                                    GuardComponent(
                                        onSuccessGuard = ParallelNode(listOf(
                                            KnockBackNode(0.5, false)
                                        ))
                                    ),
                                    PreInputReleaseComponent(listOf(Vec2(0f, Float.MAX_VALUE)), Either.left(setOf("dodge", "guard_stop"))),
                                    PreventLocalInputComponent()
                                )),
                                onAnimEnd = EmptyNode.Running
                            ),
                            BoxFollowAnimatedBoneComponent("guard", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0f, Float.MAX_VALUE)))
                        ))
                    )
                )
            )
        }

    }

}