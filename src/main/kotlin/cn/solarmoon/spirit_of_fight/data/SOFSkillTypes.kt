package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spark_core.skill.component.AnimSpeedModifierComponent
import cn.solarmoon.spark_core.skill.component.AttackDamageModifierComponent
import cn.solarmoon.spark_core.skill.component.CameraShakeComponent
import cn.solarmoon.spark_core.skill.component.MoveSetComponent
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.PreInputReleaseComponent
import cn.solarmoon.spark_core.skill.component.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.component.PreventYRotComponent
import cn.solarmoon.spark_core.skill.component.collision.BoxFollowAnimatedBoneComponent
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.collision.CommonAttackCollisionComponent
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

        fun sofKey(id: String) = ResourceKey.create(SparkRegistries.SKILL_TYPE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
    }

    init {
        val swordBoxSize = Vector3f(0.75f, 0.75f, 1.35f)
        val swordBoxOffset = Vector3f(0f, 0f, -1.35f/2)
        builder.add(SparkRegistries.SKILL_TYPE) {
            it.register(SWORD_COMBO_0,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_0"), 2, true,
                            children = listOf(
                                BoxFollowAnimatedBoneComponent("rightItem", swordBoxSize, swordBoxOffset, "anim", listOf(Vec2(0.3f, 0.45f)), listOf(
                                    CommonAttackCollisionComponent(preAttackComponents = listOf(
                                        AnimSpeedModifierComponent(),
                                        CameraShakeComponent(2, 1f, 2f)
                                    )),
                                    AttackDamageModifierComponent()
                                )),
                                MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2))), "anim"),
                                PreInputReleaseComponent("anim", listOf(Vec2(0.40f, Float.MAX_VALUE))),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(),
                                PreventMoveWithBackComponent(),
                            )),
                    ),
                    setOf()
                )
            )
            it.register(SWORD_COMBO_1,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_1"), 2, true,
                            children = listOf(
                                BoxFollowAnimatedBoneComponent("rightItem", swordBoxSize, swordBoxOffset, "anim", listOf(Vec2(0.25f, 0.40f)), listOf(
                                    CommonAttackCollisionComponent(preAttackComponents = listOf(
                                        AnimSpeedModifierComponent(),
                                        CameraShakeComponent(2, 1f, 2f)
                                    )),
                                    AttackDamageModifierComponent()
                                )),
                                MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2))), "anim"),
                                PreInputReleaseComponent("anim", listOf(Vec2(0.40f, Float.MAX_VALUE))),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(),
                                PreventMoveWithBackComponent(),

                            )),
                    ),
                    setOf()
                )
            )
            it.register(SWORD_COMBO_2,
                SkillType(
                    listOf(
                        PlayAnimationComponent(AnimIndex(ResourceLocation.withDefaultNamespace("player"), "sword:combo_2"), 2, true,
                            children = listOf(
                                BoxFollowAnimatedBoneComponent("rightItem", swordBoxSize, swordBoxOffset, "anim", listOf(Vec2(0.25f, 0.5f)), listOf(
                                    CommonAttackCollisionComponent(preAttackComponents = listOf(
                                        AnimSpeedModifierComponent(),
                                        CameraShakeComponent(3, 1.5f, 3f)
                                    )),
                                    AttackDamageModifierComponent(1.25f)
                                )),
                                MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.33))), "anim"),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(),
                                PreventMoveWithBackComponent()
                            )),
                    ),
                    setOf()
                )
            )
        }

    }

}