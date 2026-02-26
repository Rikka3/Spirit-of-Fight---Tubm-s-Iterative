package cn.solarmoon.spirit_of_fight.skill.tree.node

import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillManager
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.entity.IEntityPatch
import cn.solarmoon.spirit_of_fight.registry.common.SOFPreInputs
import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import cn.solarmoon.spirit_of_fight.skill.tree.condition.SkillTreeCondition
import cn.solarmoon.spirit_of_fight.sync.DodgeCooldownPayload
import cn.solarmoon.spirit_of_fight.sync.DodgeCostPayload
import cn.solarmoon.spirit_of_fight.sync.JumpAttackCostPayload
import cn.solarmoon.spirit_of_fight.sync.ComboCooldownCostPayload
import cn.solarmoon.spirit_of_fight.sync.SprintAttackCooldownCostPayload
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.max

/**
 * @param reserveTime 每当条件匹配但还未能切换到下一个技能节点时会刷新一个预留时间，在此时间内技能树不会重置
 */
open class CommonNode(
    override val conditions: List<SkillTreeCondition>,
    var skillLocation: ResourceLocation,
    override val preInputId: String,
    override val children: List<SkillTreeNode> = listOf(),
    override val reserveTime: Int = 0,
    override val preInputDuration: Int = 5,
    override val cooldown: Int = 0
): SkillTreeInputNode {

    override val name = Component.translatable("skill.${skillLocation.namespace}.${skillLocation.path}.name")
    override val description = Component.translatable("skill.${skillLocation.namespace}.${skillLocation.path}.description")
    override val icon: ResourceLocation = ResourceLocation.fromNamespaceAndPath(skillLocation.namespace, "textures/skill/${skillLocation.path}.png")

    override fun match(player: Player, skill: Skill?): Boolean {
        if (preInputId == SOFPreInputs.DODGE || skillLocation.path.contains("dodge") || skillLocation.path.contains("dash")) {
            if (!player.isCreative) {
                val patch = (player as Any) as? IEntityPatch
                if (patch != null) {
                    val currentTime = player.level().gameTime
                    // Check 1 second cooldown (20 ticks)
                    if (currentTime - patch.lastDodgeTick < 20) return false
                    // Check at least 2 hunger points (1 drumstick)
                    if (player.foodData.foodLevel < 2) return false
                }
            }
        }

        // Jump attack cooldown and hunger check
        if (skillLocation.path.contains("jump_attack")) {
            if (!player.isCreative) {
                val patch = (player as Any) as? IEntityPatch
                if (patch != null) {
                    val currentTime = player.level().gameTime
                    // Check 1 second cooldown (20 ticks)
                    if (currentTime - patch.lastJumpAttackTick < 20) return false
                    // Check at least 2 hunger points
                    if (player.foodData.foodLevel < 2) return false
                }
            }
        }

        // Combo cooldown check (1 second = 20 ticks)
        // Switch attacks bypass this cooldown
        if (skillLocation.path.contains("combo") && preInputId != SOFPreInputs.SWITCH_POSTURE) {
            if (!player.isCreative) {
                val patch = (player as Any) as? IEntityPatch
                if (patch != null) {
                    val currentTime = player.level().gameTime
                    // Check 1 second cooldown (20 ticks)
                    if (currentTime < patch.comboCooldownUntil) return false
                }
            }
        }

        // Sprint attack cooldown check (3 seconds = 60 ticks)
        if (skillLocation.path.contains("sprint_attack")) {
            if (!player.isCreative) {
                val patch = (player as Any) as? IEntityPatch
                if (patch != null) {
                    val currentTime = player.level().gameTime
                    // Check 3 second cooldown (60 ticks)
                    if (currentTime < patch.sprintAttackCooldownUntil) return false
                }
            }
        }

        if (cooldown > 0 && !player.isCreative) {
            val patch = (player as Any) as? IEntityPatch
            if (patch != null) {
                val lastUsed = patch.skillCooldowns[skillLocation] ?: 0L
                if (player.level().gameTime - lastUsed < cooldown) return false
            }
        }

        return super.match(player, skill)
    }

    override fun onEntry(host: Player, level: Level, tree: SkillTree): Boolean {
        if (preInputId == SOFPreInputs.DODGE || skillLocation.path.contains("dodge") || skillLocation.path.contains("dash")) {
            if (!host.isCreative) {
                if (level.isClientSide) {
                    // Send authoritative cost request to server
                    PacketDistributor.sendToServer(DodgeCostPayload)
                }
            }
        }

        // Jump attack cost - send to server for authoritative deduction
        if (skillLocation.path.contains("jump_attack")) {
            if (!host.isCreative) {
                if (level.isClientSide) {
                    PacketDistributor.sendToServer(JumpAttackCostPayload)
                }
            }
        }

        // Set combo cooldown (1 second = 20 ticks)
        if (skillLocation.path.contains("combo")) {
            if (!host.isCreative) {
                if (level.isClientSide) {
                    PacketDistributor.sendToServer(ComboCooldownCostPayload)
                }
            }
        }

        // Set sprint attack cooldown (3 seconds = 60 ticks)
        if (skillLocation.path.contains("sprint_attack")) {
            if (!host.isCreative) {
                if (level.isClientSide) {
                    PacketDistributor.sendToServer(SprintAttackCooldownCostPayload)
                }
            }
        }

        if (cooldown > 0 && !host.isCreative) {
            val patch = (host as Any) as? IEntityPatch
            patch?.skillCooldowns?.put(skillLocation, level.gameTime)
        }

        val skillType = SkillManager[skillLocation]
        if (skillType == null) {
            SpiritOfFight.LOGGER.error("Skill not found in SkillManager: $skillLocation")
            return false
        }
        tree.currentSkill = skillType.createSkill(host, level, true)
        return true
    }

    override val codec: MapCodec<out SkillTreeNode> = CODEC

    companion object {
        val CODEC: MapCodec<CommonNode> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                SkillTreeCondition.CODEC.listOf().fieldOf("conditions").forGetter { it.conditions },
                ResourceLocation.CODEC.fieldOf("skill").forGetter { it.skillLocation },
                Codec.STRING.fieldOf("pre_input_id").forGetter { it.preInputId },
                SkillTreeNode.CODEC.listOf().optionalFieldOf("children", listOf()).forGetter { it.children },
                Codec.INT.optionalFieldOf("reserve_time", 2).forGetter { it.reserveTime },
                Codec.INT.optionalFieldOf("pre_input_duration", 5).forGetter { it.preInputDuration },
                Codec.INT.optionalFieldOf("cooldown", 0).forGetter { it.cooldown }
            ).apply(instance, ::CommonNode)
        }
    }   

}