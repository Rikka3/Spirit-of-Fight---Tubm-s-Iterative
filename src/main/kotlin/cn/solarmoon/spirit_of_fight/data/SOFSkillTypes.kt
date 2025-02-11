package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spark_core.skill.component.AnimSpeedModifierComponent
import cn.solarmoon.spark_core.skill.component.AttackDamageModifierComponent
import cn.solarmoon.spark_core.skill.component.CameraShakeComponent
import cn.solarmoon.spark_core.skill.component.InvincibilityComponent
import cn.solarmoon.spark_core.skill.component.MoveSetComponent
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.PreInputReleaseComponent
import cn.solarmoon.spark_core.skill.component.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.component.PreventYRotComponent
import cn.solarmoon.spark_core.skill.component.SummonShadowComponent
import cn.solarmoon.spark_core.skill.component.collision.BoxFollowAnimatedBoneComponent
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.component.AddFightSpiritComponent
import cn.solarmoon.spirit_of_fight.skill.component.PerfectDodgeComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.CommonAttackComponent
import cn.solarmoon.spirit_of_fight.skill.component.GuardComponent
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

class SOFSkillTypes(
    private val builder: RegistrySetBuilder
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
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_0"), 2, true),
                        BoxFollowAnimatedBoneComponent("attack", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.3f, 0.45f))),
                        CommonAttackComponent(
                            preFirstAttackComponents = listOf(
                                AnimSpeedModifierComponent(),
                                CameraShakeComponent(2, 1f, 2f)
                            ),
                            preAttackComponents = listOf(
                                AddFightSpiritComponent(40)
                            )
                        ),
                        AttackDamageModifierComponent(),
                        MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2)))),
                        PreInputReleaseComponent(listOf(Vec2(0.40f, Float.MAX_VALUE))),
                        PreventLocalInputComponent(),
                        PreventYRotComponent(),
                        PreventMoveWithBackComponent(),
                    )
                )
            )
            it.register(SWORD_COMBO_1,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_1"), 2, true),
                        BoxFollowAnimatedBoneComponent("attack", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.40f))),
                        CommonAttackComponent(
                            preFirstAttackComponents = listOf(
                                AnimSpeedModifierComponent(),
                                CameraShakeComponent(2, 1f, 2f)
                            ),
                            preAttackComponents = listOf(
                                AddFightSpiritComponent(40)
                            )
                        ),
                        AttackDamageModifierComponent(),
                        MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2)))),
                        PreInputReleaseComponent(listOf(Vec2(0.40f, Float.MAX_VALUE))),
                        PreventLocalInputComponent(),
                        PreventYRotComponent(),
                        PreventMoveWithBackComponent(),
                    )
                )
            )
            it.register(SWORD_COMBO_2,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_2"), 2, true),
                        BoxFollowAnimatedBoneComponent("attack", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.5f))),
                        CommonAttackComponent(
                            preFirstAttackComponents = listOf(
                                AnimSpeedModifierComponent(),
                                CameraShakeComponent(3, 1.5f, 3f)
                            ),
                            preAttackComponents = listOf(
                                AddFightSpiritComponent(60)
                            )
                        ),
                        AttackDamageModifierComponent(1.25f),
                        MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.3f), Vec3(0.0, 0.0, 0.33)))),
                        PreventLocalInputComponent(),
                        PreventYRotComponent(),
                        PreventMoveWithBackComponent()
                    )
                )
            )
            it.register(SWORD_DODGE,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:dodge_forward"), 2, true),
                        InvincibilityComponent(listOf(Vec2(0.0f, 0.3f)), onImmuneToDamage = listOf(
                            PerfectDodgeComponent(true, listOf(Vec2(0f, 0.2f)), onPerfectDodge = listOf(
                                SummonShadowComponent()
                            ))
                        )),
                        MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.1f), Vec3(0.0, 0.0, 1.0))), true),
                        PreInputReleaseComponent(listOf(Vec2(0.35f, Float.MAX_VALUE)), Either.right(setOf("dodge", "move"))),
                        PreventLocalInputComponent(),
                        PreventYRotComponent()
                    )
                )
            )
            it.register(SWORD_GUARD,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:guard"), 15, true),
                        BoxFollowAnimatedBoneComponent("guard", "rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0f, Float.MAX_VALUE))),
                        GuardComponent(),
                        PreInputReleaseComponent(listOf(Vec2(0f, Float.MAX_VALUE)), Either.left(setOf("dodge", "guard_stop"))),
                        PreventLocalInputComponent(),
                        PreventYRotComponent()
                    )
                )
            )
        }

    }

}