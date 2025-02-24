package cn.solarmoon.spirit_of_fight.data

import cn.solarmoon.spark_core.animation.anim.origin.AnimIndex
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.skill.SkillType
import cn.solarmoon.spark_core.skill.component.AddTargetMovementComponent
import cn.solarmoon.spark_core.skill.component.AnimSpeedChangeComponent
import cn.solarmoon.spark_core.skill.component.AttackDamageModifierComponent
import cn.solarmoon.spark_core.skill.component.CameraShakeComponent
import cn.solarmoon.spark_core.skill.component.MoveSetComponent
import cn.solarmoon.spark_core.skill.component.PlaySoundComponent
import cn.solarmoon.spark_core.skill.component.PreInputReleaseComponent
import cn.solarmoon.spark_core.skill.component.PreventLocalInputComponent
import cn.solarmoon.spark_core.skill.component.PreventYRotComponent
import cn.solarmoon.spark_core.skill.component.SelfKnockBackComponent
import cn.solarmoon.spark_core.skill.component.SummonShadowComponent
import cn.solarmoon.spark_core.skill.component.body_binder.BoxAroundHolderBinder
import cn.solarmoon.spark_core.skill.component.body_binder.BoxFollowAnimatedBoneBinder
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.registry.common.SOFSounds
import cn.solarmoon.spirit_of_fight.skill.CommonAttackSkill
import cn.solarmoon.spirit_of_fight.skill.DodgeSkill
import cn.solarmoon.spirit_of_fight.skill.GuardSkill
import cn.solarmoon.spirit_of_fight.skill.SuperArmorBlockSkill
import cn.solarmoon.spirit_of_fight.skill.component.BodyFollowParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.ChargingParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.FightSpiritIncreaseComponent
import cn.solarmoon.spirit_of_fight.skill.component.GaleParticleComponent
import cn.solarmoon.spirit_of_fight.skill.component.PreventMoveWithBackComponent
import cn.solarmoon.spirit_of_fight.skill.component.QuadraticParticleComponent
import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import net.minecraft.client.particle.CampfireSmokeParticle
import net.minecraft.client.particle.DustParticle
import net.minecraft.client.particle.PlayerCloudParticle
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.awt.Color

class SOFSkillTypes(
    builder: RegistrySetBuilder
) {

    companion object {
        val SWORD_COMBO_0 = sofKey("sword_combo_0")
        val SWORD_COMBO_1 = sofKey("sword_combo_1")
        val SWORD_COMBO_2 = sofKey("sword_combo_2")
        val SWORD_SPRINTING_ATTACK = sofKey("sword_sprinting_attack")
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
        val HAMMER_SPRINTING_ATTACK = sofKey("hammer_sprinting_attack")
        val HAMMER_JUMP_ATTACK = sofKey("hammer_jump_attack")
        val HAMMER_DODGE = sofKey("hammer_dodge")
        val HAMMER_GUARD = sofKey("hammer_guard")
        val HAMMER_SPECIAL_ATTACK = sofKey("hammer_special_attack")

        fun sofKey(id: String) = ResourceKey.create(SparkRegistries.SKILL_TYPE, ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, id))
        fun entityKey(type: EntityType<*>) = BuiltInRegistries.ENTITY_TYPE.getKey(type)
    }

    init {
        val swordBoxSize = Vector3f(0.75f, 0.75f, 1.35f)
        val swordBoxOffset = Vector3f(0f, 0f, -1.35f/2)
        builder.add(SparkRegistries.SKILL_TYPE) {
            it.register(SWORD_COMBO_0,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:combo_0"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.3f, 0.45f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(Vec2(0.4f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.3f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent()
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1.1f)
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()))
                        )
                    )
                )
            )
            it.register(SWORD_COMBO_1,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:combo_1"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.4f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(Vec2(0.40f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.25f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent()
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 1f)
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()))
                        )
                    )
                )
            )
            it.register(SWORD_COMBO_2,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:combo_2"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.5f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.3f), Vec3(0.0, 0.0, 0.33)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.25f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(60),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(3, 1.5f, 3f),
                            AnimSpeedChangeComponent()
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()))
                        )
                    )
                )
            )
            it.register(SWORD_SPRINTING_ATTACK,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:attack_sprinting"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.15f, 0.45f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.2f), Vec3(0.0, 0.0, 0.25)), Pair(Vec2(0.2f, 0.3f), Vec3(0.0, 0.0, 1.0)))),
                            PreInputReleaseComponent(listOf(Vec2(0.65f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.2f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(null),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent()
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()))
                        )
                    )
                )
            )
            it.register(SWORD_JUMP_ATTACK,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:attack_jump"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.2f, 0.5f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(),
                            PreInputReleaseComponent(listOf(Vec2(0.55f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.05f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(null),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent()
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()))
                        )
                    )
                )
            )
            it.register(SWORD_DODGE,
                SkillType(
                    DodgeSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:dodge"),
                        0,
                        listOf(Vec2(0f, 0.4f)),
                        listOf(Vec2(0f, 0.2f)),
                        onAnimLifeCycle = listOf(
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.1f), Vec3(0.0, 0.0, 1.0))), true),
                            PreInputReleaseComponent(listOf(Vec2(0.35f, Float.MAX_VALUE)), Either.right(setOf("dodge", "move"))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        ),
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
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:guard"),
                        7,
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:guard_hurt"),
                        listOf(AnimIndex(entityKey(EntityType.PLAYER), "sword:guard_parry")),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset)),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        ),
                        onBlockAnimLifeCycle = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf("move")))
                        ),
                        onHurtAnimLifeCycle = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_BLOCK.value())),
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        onParryAnimLifeCycle = listOf(
                            PreInputReleaseComponent(listOf(Vec2(0.2f, Float.MAX_VALUE))),
                            PlaySoundComponent(listOf(SOFSounds.SHARP_PARRY_1.value(), SOFSounds.SHARP_PARRY_2.value(), SOFSounds.SHARP_PARRY_3.value()))
                        )
                    )
                )
            )
            it.register(SWORD_SUPER_ARMOR_BLOCK,
                SkillType(
                    SuperArmorBlockSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:skill_keeping"),
                        7,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset)),
                        listOf("sword:skill_hit"),
                        onAnimLifeCycle = listOf(
                            PreInputReleaseComponent(conditionList = Either.left(setOf("special_attack_release"))),
                            PreventLocalInputComponent(),
                            ChargingParticleComponent()
                        ),
                        onBlock = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_BLOCK.value())),
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        )
                    )
                )
            )
            it.register(SWORD_SPECIAL_ATTACK,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "sword:skill_hit"),
                        0,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", swordBoxSize, swordBoxOffset, listOf(Vec2(0.25f, 0.55f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1.5f),
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.3f), Vec3(0.0, 0.0, 0.25)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                            PreventMoveWithBackComponent(),
                            BodyFollowParticleComponent(ParticleTypes.CRIT)
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3(0.0, 0.75, 0.0)),
                            FightSpiritIncreaseComponent(0),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(5, 2f, 3f),
                            AnimSpeedChangeComponent()
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.SHARP_WIELD_1.value()), pitch = 0.8f)
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.FIREWORK)
                        ),
                        hitSound = listOf(SOFSounds.SHARP_UNDER_ATTACK_2.value())
                    )
                )
            )

            val hammerBoxSize = Vector3f(0.85f, 1.25f, 0.85f)
            val hammerBoxOffset = Vector3f(0.0f, 0.0f, -1f)
            it.register(HAMMER_COMBO_0,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_0"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(Vec2(0.4f, 0.6f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.3f, 0.5f), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(Vec2(0.7f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.4f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()),
                    )
                )
            )
            it.register(HAMMER_COMBO_1,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_1"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(Vec2(0.35f, 0.75f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.35f, 0.6f), Vec3(0.0, 0.0, 0.3)))),
                            PreInputReleaseComponent(listOf(Vec2(0.75f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.35f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_1.value())
                    )
                )
            )
            it.register(HAMMER_COMBO_2,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_2"),
                        2,
                        listOf(BoxAroundHolderBinder(Vector3f(2f, 2f, 2f), Vector3f(0f, 1f, 1.25f), listOf(Vec2(0.55f, 0.65f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1.25f),
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.65f), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.45f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(60),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(3, 2f, 3f, 5.0),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            GaleParticleComponent(ParticleTypes.CAMPFIRE_COSY_SMOKE, offset = Vec3(0.0, 0.0, 1.5)),
                            PlaySoundComponent(listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()), pitch = 0.8f)
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_2.value()),
                    )
                )
            )
            it.register(HAMMER_COMBO_C0,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_c0"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightArm", Vector3f(1f), Vector3f(), listOf(Vec2(0.25f, 0.4f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(0.5f),
                            MoveSetComponent(listOf(Pair(Vec2(0.1f, 0.35f), Vec3(0.0, 0.0, 0.35)))),
                            PreInputReleaseComponent(listOf(Vec2(0.25f, Float.MAX_VALUE)), Either.left(setOf("combo"))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.25f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SoundEvents.PLAYER_ATTACK_STRONG),
                    )
                )
            )
            it.register(HAMMER_COMBO_C1,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:combo_c1"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(Vec2(0.3f, 0.4f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.25f, 0.45f), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(listOf(Vec2(0.55f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.3f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(2, 1f, 2f),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_1.value()),
                    )
                )
            )
            it.register(HAMMER_SPRINTING_ATTACK,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:attack_sprinting"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(Vec2(0.3f, 0.9f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.3f), Vec3(0.0, 0.0, 0.25)), Pair(Vec2(0.3f, 0.5f), Vec3(0.0, 0.0, 1.0)))),
                            PreInputReleaseComponent(listOf(Vec2(1.25f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.25f, Float.MAX_VALUE))),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3.ZERO),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(3, 1.5f, 3f),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_1.value())
                    )
                )
            )
            it.register(HAMMER_JUMP_ATTACK,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:attack_jump"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(Vec2(0.17f, 0.3f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1f),
                            MoveSetComponent(),
                            PreInputReleaseComponent(listOf(Vec2(0.55f, Float.MAX_VALUE))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.05f, Float.MAX_VALUE))),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3(0.0, 1.0, 0.0)),
                            FightSpiritIncreaseComponent(40),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(3, 1.5f, 3f),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()))
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_2.value()),
                    )
                )
            )
            it.register(HAMMER_DODGE,
                SkillType(
                    DodgeSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:dodge"),
                        0,
                        listOf(Vec2(0f, 0.4f)),
                        listOf(Vec2(0f, 0.2f)),
                        onAnimLifeCycle = listOf(
                            MoveSetComponent(listOf(Pair(Vec2(0.0f, 0.1f), Vec3(0.0, 0.0, 1.0))), true),
                            PreInputReleaseComponent(listOf(Vec2(0.35f, Float.MAX_VALUE)), Either.right(setOf("dodge", "move"))),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(),
                        ),
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
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:guard"),
                        7,
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:guard_hurt"),
                        listOf(),
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", Vector3f(0.65f, 0.65f, 2.0f), Vector3f(0.0f, 0.0f, -0.5f))),
                        onSkillLifeCycle = listOf(
                            PreventLocalInputComponent()
                        ),
                        onBlockAnimLifeCycle = listOf(
                            PreInputReleaseComponent(conditionList = Either.right(setOf("move")))
                        ),
                        onHurtAnimLifeCycle = listOf(
                            SelfKnockBackComponent(0.5, false),
                            PreventYRotComponent(),
                            CameraShakeComponent(2, 1f, 2f),
                            PlaySoundComponent(listOf(SoundEvents.SHIELD_BLOCK)),
                            QuadraticParticleComponent(ParticleTypes.CRIT)
                        )
                    )
                )
            )
            it.register(HAMMER_SPECIAL_ATTACK,
                SkillType(
                    CommonAttackSkill(
                        AnimIndex(entityKey(EntityType.PLAYER), "hammer:skill"),
                        2,
                        listOf(BoxFollowAnimatedBoneBinder("rightItem", hammerBoxSize, hammerBoxOffset, listOf(Vec2(0.75f, 0.95f)))),
                        onAnimLifeCycle = listOf(
                            AttackDamageModifierComponent(1.5f),
                            MoveSetComponent(listOf(Pair(Vec2(0.65f, 0.9f), Vec3(0.0, 0.0, 0.2)))),
                            PreInputReleaseComponent(conditionList = Either.left(setOf())),
                            PreventLocalInputComponent(),
                            PreventYRotComponent(listOf(Vec2(0.45f, Float.MAX_VALUE))),
                            PreventMoveWithBackComponent(),
                        ),
                        onPreAttack = listOf(
                            AddTargetMovementComponent(Vec3(0.0, 1.0, 0.0)),
                            FightSpiritIncreaseComponent(0),
                        ),
                        onPreFirstAttack = listOf(
                            CameraShakeComponent(5, 2.5f, 3f, 5.0),
                            AnimSpeedChangeComponent(),
                        ),
                        onBoxActive = listOf(
                            GaleParticleComponent(ParticleTypes.CAMPFIRE_COSY_SMOKE),
                            PlaySoundComponent(listOf(SOFSounds.HARD_WIELD_1.value()), pitch = 0.8f)
                        ),
                        onSuccessHit = listOf(
                            QuadraticParticleComponent(ParticleTypes.FIREWORK)
                        ),
                        hitSound = listOf(SOFSounds.HARD_UNDER_ATTACK_2.value()),
                    )
                )
            )
        }

    }

}