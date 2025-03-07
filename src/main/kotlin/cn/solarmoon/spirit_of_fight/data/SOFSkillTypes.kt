package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.molang.core.value.DoubleValue
import cn.solarmoon.spark_core.molang.core.value.MolangValue
import cn.solarmoon.spark_core.molang.core.value.Vector3k
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillTimeLine
import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spark_core.skill.component.AddTargetMovementComponent
import cn.solarmoon.spark_core.skill.component.AnimSpeedChangeComponent
import cn.solarmoon.spark_core.skill.component.AttackDamageModifierComponent
import cn.solarmoon.spark_core.skill.component.CameraShakeComponent
import cn.solarmoon.spark_core.skill.component.MovementComponent
import cn.solarmoon.spark_core.skill.component.PhysicsBodyAttackComponent
import cn.solarmoon.spark_core.skill.component.PlayAnimationComponent
import cn.solarmoon.spark_core.skill.component.PlaySoundComponent
import cn.solarmoon.spark_core.skill.component.PreInputReleaseComponent
import cn.solarmoon.spark_core.skill.component.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.component.PreventYRotComponent
import cn.solarmoon.spark_core.skill.component.SelfKnockBackComponent
import cn.solarmoon.spark_core.skill.component.SummonShadowComponent
import cn.solarmoon.spark_core.skill.component.body_binder.BoxAroundHolderBinder
import cn.solarmoon.spark_core.skill.component.body_binder.BoxFollowAnimatedBoneBinder
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.hit.SOFHitTypes
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import cn.solarmoon.spirit_of_fight.skill.AnimSkill
import cn.solarmoon.spirit_of_fight.skill.AttackSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.GuardSkill
import cn.solarmoon.spirit_of_fight.skill.SuperArmorBlockSkill
import cn.solarmoon.spirit_of_fight.skill.component.BodyFollowParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.ChargingParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.FightSpiritIncreaseComponent
import cn.solarmoon.spirit_of_fight.skill.component.GaleParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.FreezeAnimUntilHitComponent
import cn.solarmoon.spirit_of_fight.skill.component.FreezeAnimUntilOnGroundComponent
import cn.solarmoon.spirit_of_fight.skill.component.GrabComponent
import cn.solarmoon.spirit_of_fight.skill.component.SetGrabComponent
import cn.solarmoon.spirit_of_fight.skill.component.QuadraticHitParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.UpdateSwimPoseComponent
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class SOFSkillTypes(
    builder: RegistrySetBuilder
) {

    companion object {
        val SHIELD_COMBO_C0 = sofKey("shield_combo_c0")
        val SHIELD_GUARD = sofKey("shield_guard")

        val SWORD_COMBO_0 = sofKey("sword_combo_0")
        val SWORD_COMBO_1 = sofKey("sword_combo_1")
        val SWORD_COMBO_2 = sofKey("sword_combo_2")
        val SWORD_SHIELD_COMBO_C1 = sofKey("sword_shield_combo_c1")
        val SWORD_SPRINT_ATTACK = sofKey("sword_sprint_attack")
        val SWORD_JUMP_ATTACK = sofKey("sword_jump_attack")
        val SWORD_DODGE = sofKey("sword_dodge")
        val SWORD_GUARD = sofKey("sword_guard")
        val SWORD_SUPER_ARMOR_BLOCK = sofKey("sword_super_armor_block")
        val SWORD_SPECIAL_ATTACK = sofKey("sword_special_attack")

        val HAMMER_COMBO_0 = sofKey("hammer_combo_0")
        val HAMMER_COMBO_1 = sofKey("hammer_combo_1")
        val HAMMER_COMBO_2 = sofKey("hammer_combo_2")
        val HAMMER_COMBO_C0 = sofKey("hammer_combo_c0")
        val HAMMER_COMBO_C1 = sofKey("hammer_combo_c1")
        val HAMMER_SPRINT_ATTACK = sofKey("hammer_sprint_attack")
        val HAMMER_JUMP_ATTACK = sofKey("hammer_jump_attack")
        val HAMMER_DODGE = sofKey("hammer_dodge")
        val HAMMER_GUARD = sofKey("hammer_guard")
        val HAMMER_SPECIAL_ATTACK = sofKey("hammer_special_attack")

        val AXE_COMBO_0 = sofKey("axe_combo_0")
        val AXE_COMBO_1 = sofKey("axe_combo_1")
        val AXE_COMBO_2 = sofKey("axe_combo_2")
        val AXE_SHIELD_COMBO_C1 = sofKey("axe_shield_combo_c1")
        val AXE_SHIELD_COMBO_C2 = sofKey("axe_shield_combo_c2")
        val AXE_SPRINT_ATTACK = sofKey("axe_sprint_attack")
        val AXE_JUMP_ATTACK = sofKey("axe_jump_attack")
        val AXE_DODGE = sofKey("axe_dodge")
        val AXE_GUARD = sofKey("axe_guard")
        val AXE_SKILL_GRAB = sofKey("axe_grab")
        val AXE_SKILL_PULL = sofKey("axe_pull")
        val AXE_SKILL_SPIN = sofKey("axe_spin")

        val GLOVES_COMBO_0 = sofKey("gloves_combo_0")
        val GLOVES_COMBO_1 = sofKey("gloves_combo_1")
        val GLOVES_COMBO_2 = sofKey("gloves_combo_2")
        val GLOVES_SPRINT_ATTACK = sofKey("gloves_sprint_attack")
        val GLOVES_JUMP_ATTACK = sofKey("gloves_jump_attack")
        val GLOVES_DODGE = sofKey("gloves_dodge")
        val GLOVES_GUARD = sofKey("gloves_guard")
        val GLOVES_SKILL_0 = sofKey("gloves_skill_0")
        val GLOVES_SKILL_1 = sofKey("gloves_skill_1")

        val MACE_COMBO_0 = sofKey("mace_combo_0")
        val MACE_COMBO_1 = sofKey("mace_combo_1")
        val MACE_COMBO_2 = sofKey("mace_combo_2")
        val MACE_SHIELD_COMBO_C1 = sofKey("mace_shield_combo_c1")
        val MACE_SPRINT_ATTACK = sofKey("mace_sprint_attack")
        val MACE_JUMP_ATTACK = sofKey("mace_jump_attack")
        val MACE_DODGE = sofKey("mace_dodge")
        val MACE_GUARD = sofKey("mace_guard")
        val MACE_SKILL_CHARGING = sofKey("mace_skill_charging")
        val MACE_SKILL = sofKey("mace_skill")
        val MACE_FALL_ATTACK = sofKey("mace_fall_attack")

        fun sofKey(id: String) = ResourceKey.create(SparkRegistries.SKILL_TYPE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
        fun entityKey(type: EntityType<*>) = BuiltInRegistries.ENTITY_TYPE.getKey(type)
    }

    init {
        builder.add(SparkRegistries.SKILL_TYPE) {
            val shieldBoxSize = Vector3f(0.5f, 1.0f, 1.55f)
            val shieldBoxOffset = Vector3f(-0.25f, 0.0f, -0.25f)
            it.register(SHIELD_COMBO_C0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "shield:combo_c0"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(0.25f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.4), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0)), Either.left(setOf(SOFPreInputs.ATTACK))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.3, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("leftItem", shieldBoxSize, shieldBoxOffset, listOf(SkillTimeLine.Stamp(0.3, 0.5)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_SWEEP), pitch = 0.85f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_KNOCKBACK), pitch = 0.75f)
                            )
                        ),
                        SOFHitTypes.LIGHT_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(SHIELD_GUARD,
                SkillType(
                    GuardSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "shield:guard"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf(SOFPreInputs.MOVE)))
                        )),
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "shield:guard_hurt"), 0, onAnimStart = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SoundEvents.SHIELD_BLOCK))
                        )),
                        listOf(AnimIndex(entityKey(EntityType.PLAYER), "shield:guard_parry")),
                        listOf(BoxFollowAnimatedBoneBinder("leftItem", shieldBoxSize, shieldBoxOffset)),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        ),
                        onParryAnimLifeCycle = listOf(
                            AddTargetMovementComponent(Vec3(0.0, 0.0, 1.0)),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                            PlaySoundComponent(listOf(SoundEvents.SHIELD_BLOCK), pitch = 0.8f)
                        )
                    )
                )
            )

            val swordBoxSize = Vector3f(0.75f, 0.75f, 1.35f)
            val swordBoxOffset = Vector3f(0f, 0f, -1.35f/2)
            it.register(SWORD_COMBO_0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:combo_0"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.4, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.3, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.3, 0.45)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1.1f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()), pitch = 1.1f)
                            )
                        ),
                        SOFHitTypes.LIGHT_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(SWORD_COMBO_1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:combo_1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.40, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.4)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(SWORD_COMBO_2,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:combo_2"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.3), Vec3(0.0, 0.0, 0.33)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.5)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(60),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()), pitch = 0.9f)
                            )
                        ),
                        SOFHitTypes.HEAVY_STAB.toString().lowercase()
                    )
                )
            )
            it.register(SWORD_SHIELD_COMBO_C1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:shield_combo_c1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.3, 0.55), Vec3(0.0, 0.0, 0.25)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.60, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.3, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.3, 0.55)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(SWORD_SPRINT_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:attack_sprinting"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.2), Vec3(0.0, 0.0, 0.25)), Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 1.0)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.65, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.45)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(SWORD_JUMP_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:attack_jump"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.05, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.2, 0.5)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(SWORD_DODGE,
                SkillType(
                    DodgeSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:dodge"), 0, onAnimStart = listOf(
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.1), Vec3(0.0, 0.0, 1.0))), true),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.35, 100.0)), Either.right(setOf(SOFPreInputs.DODGE, SOFPreInputs.MOVE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        )),
                        listOf(SkillTimeLine.Stamp(0.0, 0.4)),
                        listOf(SkillTimeLine.Stamp(0.0, 0.2)),
                        onPerfectDodge = listOf(
                            SummonShadowComponent(),
                            PlaySoundComponent(listOf(SOFSounds.PERFECT_DODGE.value()))
                        )
                    )
                )
            )
            it.register(SWORD_GUARD,
                SkillType(
                    GuardSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:guard"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf(SOFPreInputs.MOVE)))
                        )),
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:guard_hurt"), 0, onAnimStart = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_BLOCK.value()))
                        )),
                        listOf(AnimIndex(entityKey(EntityType.PLAYER), "sword:guard_parry")),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset)),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        ),
                        onParryAnimLifeCycle = listOf(
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_PARRY_1.value(), SOFSounds.SHARP_PARRY_2.value(), SOFSounds.SHARP_PARRY_3.value()))
                        )
                    )
                )
            )
            it.register(SWORD_SUPER_ARMOR_BLOCK,
                SkillType(
                    SuperArmorBlockSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:skill_keeping"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.left(setOf(SOFPreInputs.SPECIAL_ATTACK, SOFPreInputs.STOP))),
                            PreventLocalInputComponent(),
                            ChargingParticleComponent()
                        )),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset)),
                        listOf("sword:skill_hit"),
                        onBlock = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_BLOCK.value()))
                        )
                    )
                )
            )
            it.register(SWORD_SPECIAL_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:skill_hit"), 0, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.5f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.3), Vec3(0.0, 0.0, 0.25)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                            PreventMoveWithBackComponent(),
                            BodyFollowParticleComponent(ParticleTypes.CRIT)
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.55)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                AddTargetMovementComponent(Vec3(0.0, 0.75, 0.0)),
                                FightSpiritIncreaseComponent(0),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(5, 2f, 3f),
                                AnimSpeedChangeComponent(),
                                QuadraticHitParticleComponent(ParticleTypes.FIREWORK, false),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_2.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_UPSTROKE.toString().lowercase()
                    )
                )
            )

            val hammerBoxSize = Vector3f(0.85f, 1.25f, 0.85f)
            val hammerBoxOffset = Vector3f(0.0f, 0.0f, -1f)
            it.register(HAMMER_COMBO_0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_0"), 2, listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.3, 0.5), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.7, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.4, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(SkillTimeLine.Stamp(0.4, 0.6)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_COMBO_1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.35, 0.6), Vec3(0.0, 0.0, 0.3)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.75, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.35, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(SkillTimeLine.Stamp(0.35, 0.75)), listOf(
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_COMBO_2,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_2"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.65), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.45, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxAroundHolderBinder(Vector3f(2f, 2f, 2f), Vector3f(0f, 1f, 1.25f), listOf(SkillTimeLine.Stamp(0.55, 0.65)), onBodyActive = listOf(
                                GaleParticleComponent(ParticleTypes.CAMPFIRE_COSY_SMOKE, offset = Vec3(0.0, 0.0, 1.5)),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(60),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 2f, 3f, 5.0),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_2.value()))
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_COMBO_C0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_c0"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(0.5f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.1, 0.35), Vec3(0.0, 0.0, 0.35)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0)), Either.left(setOf(SOFPreInputs.ATTACK))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightArm", Vector3f(1f), Vector3f(), listOf(SkillTimeLine.Stamp(0.25, 0.4)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_KNOCKBACK), pitch = 0.75f)
                            )
                        ),
                        SOFHitTypes.HEAVY_STAB.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_COMBO_C1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_c1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.25, 0.45), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.4, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(SkillTimeLine.Stamp(0.4, 0.6)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_SPRINT_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:attack_sprinting"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.3), Vec3(0.0, 0.0, 0.25)), Pair(SkillTimeLine.Stamp(0.3, 0.5), Vec3(0.0, 0.0, 1.0)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(1.25, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(SkillTimeLine.Stamp(0.3, 0.9)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_JUMP_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:attack_jump"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.05, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(SkillTimeLine.Stamp(0.17, 0.3)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent(),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_2.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(HAMMER_DODGE,
                SkillType(
                    DodgeSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:dodge"), 0, onAnimStart = listOf(
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.1), Vec3(0.0, 0.0, 1.0))), true),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.35, 100.0)), Either.right(setOf(SOFPreInputs.DODGE, SOFPreInputs.MOVE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        )),
                        listOf(SkillTimeLine.Stamp(0.0, 0.4)),
                        listOf(SkillTimeLine.Stamp(0.0, 0.2)),
                        onPerfectDodge = listOf(
                            SummonShadowComponent(),
                            PlaySoundComponent(listOf(SOFSounds.PERFECT_DODGE.value()))
                        )
                    )
                )
            )
            it.register(HAMMER_GUARD,
                SkillType(
                    GuardSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:guard"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf(SOFPreInputs.MOVE)))
                        )),
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:guard_hurt"), 0, onAnimStart = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SoundEvents.SHIELD_BLOCK))
                        )),
                        listOf(),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", Vector3f(0.65f, 0.65f, 2.0f), Vector3f(0.0f, 0.0f, -0.5f))),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        )
                    )
                )
            )
            it.register(HAMMER_SPECIAL_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "hammer:skill"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.5f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.65, 0.9), Vec3(0.0, 0.0, 0.35)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.45, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(SkillTimeLine.Stamp(0.75, 0.95)), onBodyActive = listOf(
                                GaleParticleComponent(ParticleTypes.CAMPFIRE_COSY_SMOKE),
                                PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                AddTargetMovementComponent(Vec3(0.0, 1.0, 0.0)),
                                FightSpiritIncreaseComponent(0),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(5, 2.5f, 3f, 5.0),
                                AnimSpeedChangeComponent(),
                                QuadraticHitParticleComponent(ParticleTypes.FIREWORK, false),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_2.value()))
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_UPSTROKE.toString().lowercase()
                    )
                )
            )

            val axeBoxSize = Vector3f(0.75f, 0.75f, 0.75f)
            val axeBoxOffset = Vector3f(0.0f, 0.0f, -0.55f)
            it.register(AXE_COMBO_0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:combo_0"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.5)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1.1f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()), pitch = 1.1f)
                            )
                        ),
                        SOFHitTypes.LIGHT_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(AXE_COMBO_1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:combo_1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.2, 0.45)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()), pitch = 1f)
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(AXE_COMBO_2,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:combo_2"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.45, 0.65), Vec3(0.0, 0.0, 0.25)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.45, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.45, 0.7)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(60),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()), pitch = 0.9f)
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(AXE_SHIELD_COMBO_C1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:shield_combo_c1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.3, 0.55), Vec3(0.0, 0.0, 0.25)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.60, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.3, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.3, 0.55)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(AXE_SHIELD_COMBO_C2,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:shield_combo_c2"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.45, 0.65), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.45, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.45, 0.7)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(60),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()), pitch = 0.9f)
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(AXE_SPRINT_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:attack_sprinting"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.2), Vec3(0.0, 0.0, 0.25)), Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 1.0)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.75, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.5)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(AXE_JUMP_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:attack_jump"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.05, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.45)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(AXE_DODGE,
                SkillType(
                    DodgeSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:dodge"), 0, onAnimStart = listOf(
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.1), Vec3(0.0, 0.0, 1.0))), true),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.35, 100.0)), Either.right(setOf(SOFPreInputs.DODGE, SOFPreInputs.MOVE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        )),
                        listOf(SkillTimeLine.Stamp(0.0, 0.4)),
                        listOf(SkillTimeLine.Stamp(0.0, 0.2)),
                        onPerfectDodge = listOf(
                            SummonShadowComponent(),
                            PlaySoundComponent(listOf(SOFSounds.PERFECT_DODGE.value()))
                        )
                    )
                )
            )
            it.register(AXE_GUARD,
                SkillType(
                    GuardSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:guard"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf(SOFPreInputs.MOVE)))
                        )),
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:guard_hurt"), 0, onAnimStart = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_BLOCK.value()))
                        )),
                        listOf(AnimIndex(entityKey(EntityType.PLAYER), "axe:guard_parry")),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset)),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        ),
                        onParryAnimLifeCycle = listOf(
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_PARRY_1.value(), SOFSounds.SHARP_PARRY_2.value(), SOFSounds.SHARP_PARRY_3.value()))
                        )
                    )
                )
            )
            it.register(AXE_SKILL_GRAB,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:skill"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(0.5f),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.4, 100.0))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.4, 100.0)), conditionList = Either.left(setOf(SOFPreInputs.SPECIAL_ATTACK)))
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.4, 0.6)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(20),
                                AddTargetMovementComponent()
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                SetGrabComponent(),
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(AXE_SKILL_PULL,
                SkillType(
                    AnimSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:skill_pull"), 0,
                            onAnimStart = listOf(
                                GrabComponent("rightItem", Vec3(0.0, -0.65, -0.65)),
                                PreventLocalInputComponent(),
                                PreventYRotComponent(),
                                PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.0, 0.25)), Either.left(setOf(SOFPreInputs.SPECIAL_ATTACK)))
                            ),
                            onAnimEnd = listOf(
                                SetGrabComponent.Remove()
                            )
                        ),
                    ),
                )
            )
            it.register(AXE_SKILL_SPIN,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "axe:skill_hit"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.5f),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.0, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", axeBoxSize, axeBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.35)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.5f)
                            ))),
                            onPreAttack = listOf(
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent(),
                                QuadraticHitParticleComponent(ParticleTypes.FIREWORK, false),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_5.value()))
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_SWIPE.toString().lowercase()
                    )
                )
            )

            val glovesBoxSize = Vector3f(1f, 1f, 1f)
            val glovesBoxOffset = Vector3f(0f, 0f, 0f)
            it.register(GLOVES_COMBO_0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:combo_0"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(
                                Pair(SkillTimeLine.Stamp(0.1, 0.2), Vec3(0.0, 0.0, 0.35)),
                                Pair(SkillTimeLine.Stamp(0.4, 0.5), Vec3(0.0, 0.0, 0.2))
                            )),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.4, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.15, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(
                                BoxFollowAnimatedBoneBinder("rightItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.2)), onBodyActive = listOf(
                                    PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_WEAK))
                                )),
                                BoxFollowAnimatedBoneBinder("leftItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.4)), onBodyActive = listOf(
                                    PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_STRONG))
                                ))
                            ),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f)
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SOFT_UNDER_ATTACK_3.value()), pitch = 1.1f)
                            )
                        ),
                        SOFHitTypes.LIGHT_STAB.toString().lowercase()
                    )
                )
            )
            it.register(GLOVES_COMBO_1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:combo_1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(
                                Pair(SkillTimeLine.Stamp(0.05, 0.2), Vec3(0.0, 0.0, 0.1)),
                                Pair(SkillTimeLine.Stamp(0.35, 0.5), Vec3(0.0, 0.0, 0.45))
                            )),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.45, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.15, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(
                                BoxFollowAnimatedBoneBinder("rightItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.3)), onBodyActive = listOf(
                                    PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_WEAK))
                                )),
                                BoxFollowAnimatedBoneBinder("leftItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.35, 0.45)), onBodyActive = listOf(
                                    PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_STRONG))
                                ))
                            ),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f)
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SOFT_UNDER_ATTACK_3.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(GLOVES_COMBO_2,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:combo_2"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(
                                Pair(SkillTimeLine.Stamp(0.15, 0.55), Vec3(0.0, 0.0, 0.35))
                            )),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.5, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(
                                BoxFollowAnimatedBoneBinder("leftItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.5, 0.7)), onBodyActive = listOf(
                                    PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_STRONG))
                                ))
                            ),
                            onPreAttack = listOf(

                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f)
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SOFT_UNDER_ATTACK_4.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(GLOVES_SPRINT_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:attack_sprinting"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(
                                Pair(SkillTimeLine.Stamp(0.0, 0.65), Vec3(0.0, 0.0, 0.6))
                            )),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.95, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.05, 100.0))),
                            PreventMoveWithBackComponent(),
                            UpdateSwimPoseComponent()
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(
                                BoxFollowAnimatedBoneBinder("leftLeg", Vector3f(1.0f, 1.0f, 1.0f), Vector3f(0.0f, -0.5f, 0.0f), listOf(SkillTimeLine.Stamp(0.05, 0.6)), onBodyActive = listOf(
                                    PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_SWEEP))
                                ))
                            ),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f)
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_KNOCKBACK))
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_STAB.toString().lowercase()
                    )
                )
            )
            it.register(GLOVES_JUMP_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:attack_jump"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.5, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.0, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightLeg", Vector3f(1.0f, 1.0f, 1.0f), Vector3f(0.0f, -0.5f, 0.0f), listOf(SkillTimeLine.Stamp(0.0, 0.3)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_STRONG))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SOFT_UNDER_ATTACK_3.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_STAB.toString().lowercase()
                    )
                )
            )
            it.register(GLOVES_DODGE,
                SkillType(
                    DodgeSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:dodge"), 0, onAnimStart = listOf(
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.1), Vec3(0.0, 0.0, 0.5))), true),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.35, 100.0)), Either.right(setOf(SOFPreInputs.DODGE, SOFPreInputs.MOVE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        )),
                        listOf(SkillTimeLine.Stamp(0.0, 0.4)),
                        listOf(SkillTimeLine.Stamp(0.0, 0.2)),
                        onPerfectDodge = listOf(
                            SummonShadowComponent(),
                            PlaySoundComponent(listOf(SOFSounds.PERFECT_DODGE.value()))
                        )
                    )
                )
            )
            it.register(GLOVES_GUARD,
                SkillType(
                    GuardSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:guard"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf(SOFPreInputs.MOVE)))
                        )),
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:guard_hurt"), 0, onAnimStart = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SOFSounds.SOFT_BLOCK.value()))
                        )),
                        listOf(),
                        listOf(BoxFollowAnimatedBoneBinder("leftItem", glovesBoxSize, glovesBoxOffset)),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        )
                    )
                )
            )
            it.register(GLOVES_SKILL_0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:skill_keeping"), 0, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.5, 100.0)), conditionList = Either.left(setOf(SOFPreInputs.SPECIAL_ATTACK, SOFPreInputs.STOP))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.15, 100.0))),
                            PreventMoveWithBackComponent(),
                            BodyFollowParticleComponent(ParticleTypes.CRIT)
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("leftItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.35)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_STRONG))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(20)
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(5, 2f, 3f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                QuadraticHitParticleComponent(ParticleTypes.WHITE_SMOKE),
                                PlaySoundComponent(listOf(SOFSounds.SOFT_UNDER_ATTACK_2.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(GLOVES_SKILL_1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "baimei:skill_hit"), 0, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.5f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.1, 0.25), Vec3(0.0, 0.0, 0.85)))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.15, 100.0))),
                            PreventMoveWithBackComponent(),
                            BodyFollowParticleComponent(ParticleTypes.CRIT)
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", glovesBoxSize, glovesBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.35)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SoundEvents.PLAYER_ATTACK_STRONG))
                            ))),
                            onPreAttack = listOf(
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(5, 2f, 3f),
                                AnimSpeedChangeComponent(),
                                QuadraticHitParticleComponent(ParticleTypes.FIREWORK, false),
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SOFT_UNDER_ATTACK_1.value()))
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_SWIPE.toString().lowercase()
                    )
                )
            )

            val maceBoxSize = Vector3f(0.95f, 0.95f, 0.95f)
            val maceBoxOffset = Vector3f(0.0f, 0.0f, -0.65f)
            it.register(MACE_COMBO_0,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:combo_0"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.65, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.5)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1.1f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()), pitch = 1.1f)
                            )
                        ),
                        SOFHitTypes.LIGHT_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(MACE_COMBO_1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:combo_1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.6, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.25, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.45)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1f)
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(MACE_COMBO_2,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:combo_2"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.7, 0.95), Vec3(0.0, 0.2, 0.0)))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.65, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.65, 0.9)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                            ))),
                            onPreAttack = listOf(
                                AddTargetMovementComponent(Vec3(0.0, 0.75, 0.0)),
                                FightSpiritIncreaseComponent(60),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(3, 1.5f, 3f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()), pitch = 0.9f)
                            )
                        ),
                        SOFHitTypes.HEAVY_UPSTROKE.toString().lowercase()
                    )
                )
            )
            it.register(MACE_SHIELD_COMBO_C1,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:shield_combo_c1"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.3, 0.55), Vec3(0.0, 0.0, 0.25)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.60, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.3, 100.0))),
                            PreventMoveWithBackComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.3, 0.55)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()))
                            )
                        ),
                        SOFHitTypes.LIGHT_SWIPE.toString().lowercase()
                    )
                )
            )
            it.register(MACE_SPRINT_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:attack_sprinting"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.2), Vec3(0.0, 0.0, 0.25)), Pair(SkillTimeLine.Stamp(0.2, 0.3), Vec3(0.0, 0.0, 1.0)))),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.65, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.15, 0.45)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()))
                            )
                        )
                    )
                )
            )
            it.register(MACE_JUMP_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:attack_jump"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.55, 100.0))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(0.05, 100.0))),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.4)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(40),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()))
                            )
                        ),
                        SOFHitTypes.HEAVY_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(MACE_DODGE,
                SkillType(
                    DodgeSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:dodge"), 0, onAnimStart = listOf(
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.1), Vec3(0.0, 0.0, 1.0))), true),
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.35, 100.0)), Either.right(setOf(SOFPreInputs.DODGE, SOFPreInputs.MOVE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        )),
                        listOf(SkillTimeLine.Stamp(0.0, 0.4)),
                        listOf(SkillTimeLine.Stamp(0.0, 0.2)),
                        onPerfectDodge = listOf(
                            SummonShadowComponent(),
                            PlaySoundComponent(listOf(SOFSounds.PERFECT_DODGE.value()))
                        )
                    )
                )
            )
            it.register(MACE_GUARD,
                SkillType(
                    GuardSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:guard"), 7, onAnimStart = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf(SOFPreInputs.MOVE)))
                        )),
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:guard_hurt"), 0, onAnimStart = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_BLOCK.value()))
                        )),
                        listOf(AnimIndex(entityKey(EntityType.PLAYER), "mace:guard_parry")),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset)),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        ),
                        onParryAnimLifeCycle = listOf(
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.2, 100.0))),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_PARRY_1.value(), SOFSounds.SHARP_PARRY_2.value(), SOFSounds.SHARP_PARRY_3.value()))
                        )
                    )
                )
            )
            it.register(MACE_FALL_ATTACK,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:attack_jump_special"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1f),
                            MovementComponent(listOf(
                                Pair(SkillTimeLine.Stamp(0.15, 0.45), Vec3(0.0, 0.25, 0.0)),
                                Pair(SkillTimeLine.Stamp(0.5, 0.55), Vec3(0.0, -1.0, 0.0))
                            )),
                            PreventLocalInputComponent(activeTime = listOf(SkillTimeLine.Stamp(1.1, 100.0))),
                            PreventYRotComponent(listOf(SkillTimeLine.Stamp(1.1, 100.0))),
                            FreezeAnimUntilHitComponent(1.0),
                            FreezeAnimUntilOnGroundComponent(1.0)
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.8, 1.05)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                FightSpiritIncreaseComponent(100),
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT)
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_CHOP.toString().lowercase()
                    )
                )
            )
            it.register(MACE_SKILL_CHARGING,
                SkillType(
                    AnimSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:skill_keeping"), 2, onAnimStart = listOf(
                            PreInputReleaseComponent(listOf(SkillTimeLine.Stamp(0.5, 100.0)), conditionList = Either.left(setOf(SOFPreInputs.SPECIAL_ATTACK, SOFPreInputs.STOP))),
                            PreventLocalInputComponent()
                        ))
                    )
                )
            )
            it.register(MACE_SKILL,
                SkillType(
                    AttackSkill(
                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "mace:skill_hit"), 2, onAnimStart = listOf(
                            AttackDamageModifierComponent(1.5f),
                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.05, 0.3), Vector3k(DoubleValue(0.0), DoubleValue(0.0), MolangValue.parse("q.charging_time / 10"))))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        )),
                        PhysicsBodyAttackComponent(
                            listOf(BoxFollowAnimatedBoneBinder("rightItem", maceBoxSize, maceBoxOffset, listOf(SkillTimeLine.Stamp(0.05, 0.7)), onBodyActive = listOf(
                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                            ))),
                            onPreAttack = listOf(
                                AddTargetMovementComponent(Vec3(0.0, 0.5, 1.5))
                            ),
                            onPreFirstAttack = listOf(
                                CameraShakeComponent(2, 1f, 2f),
                                AnimSpeedChangeComponent()
                            ),
                            onActualHit = listOf(
                                QuadraticHitParticleComponent(ParticleTypes.CRIT),
                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_4.value()))
                            )
                        ),
                        SOFHitTypes.KNOCKDOWN_STAB.toString().lowercase()
                    )
                )
            )

//            it.register(SWORD_SPECIAL_ATTACK,
//                SkillType(
//                    AttackSkill(
//                        PlayAnimationComponent(AnimIndex(entityKey(EntityType.PLAYER), "sword:skill_hit"), 0, onAnimStart = listOf(
//                            AttackDamageModifierComponent(1.5f),
//                            MovementComponent(listOf(Pair(SkillTimeLine.Stamp(0.0, 0.3), Vec3(0.0, 0.0, 0.25)))),
//                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
//                            PreventLocalInputComponent(),
//                            PreventYRotComponent(),
//                            PreventMoveWithBackComponent(),
//                            BodyFollowParticleComponent(ParticleTypes.CRIT)
//                        )),
//                        PhysicsBodyAttackComponent(
//                            listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(SkillTimeLine.Stamp(0.25, 0.55)), onBodyActive = listOf(
//                                PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
//                            ))),
//                            onPreAttack = listOf(
//                                AddTargetMovementComponent(Vec3(0.0, 0.75, 0.0)),
//                                FightSpiritIncreaseComponent(0),
//                            ),
//                            onPreFirstAttack = listOf(
//                                CameraShakeComponent(5, 2f, 3f),
//                                AnimSpeedChangeComponent()
//                            ),
//                            onActualHit = listOf(
//                                QuadraticHitParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())),
//                                QuadraticHitParticleComponent(ParticleTypes.FIREWORK),
//                                PlaySoundComponent(listOf(SOFSounds.SHARP_UNDER_ATTACK_2.value()))
//                            )
//                        )
//                    )
//                )
//            )
        }
    }

}