package cn.solarmoon.spirit_of_fight.command

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.data.WeaponSkillOverrideManager
import cn.solarmoon.spirit_of_fight.registry.common.SOFRegistries
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

object WeaponOverrideCommand {

    private val ITEM_SUGGESTIONS: SuggestionProvider<CommandSourceStack> = SuggestionProvider { ctx, builder ->
        val server = ctx.source.server
        if (server != null) {
            val itemRegistry = server.registryAccess().registry(BuiltInRegistries.ITEM.key()).orElse(null)
            if (itemRegistry != null) {
                itemRegistry.keySet().forEach { key ->
                    builder.suggest(key.toString())
                }
            }
        }
        builder.buildFuture()
    }

    private val SKILL_TREE_SUGGESTIONS: SuggestionProvider<CommandSourceStack> = SuggestionProvider { ctx, builder ->
        val server = ctx.source.server
        if (server != null) {
            val registry = server.registryAccess().registry(SOFRegistries.SKILL_TREE).orElse(null)
            if (registry != null) {
                registry.keySet().forEach { key ->
                    if (key.namespace == SpiritOfFight.MOD_ID) {
                        builder.suggest(key.path)
                        builder.suggest(key.toString())
                    } else {
                        builder.suggest(key.toString())
                    }
                }
            }
        }
        builder.buildFuture()
    }

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("sof_weapon_override")
                .requires { it.hasPermission(2) }

                // Set item override
                .then(
                    Commands.literal("set")
                        .then(
                            Commands.literal("item")
                                .then(
                                    Commands.argument("item_id", ResourceLocationArgument.id())
                                        .suggests(ITEM_SUGGESTIONS)
                                        .then(
                                            Commands.argument("skill_tree", StringArgumentType.word())
                                                .suggests(SKILL_TREE_SUGGESTIONS)
                                                .executes { ctx ->
                                                    val itemId = ResourceLocationArgument.getId(ctx, "item_id")
                                                    val skillTree = StringArgumentType.getString(ctx, "skill_tree")
                                                    executeSetItem(ctx, itemId, skillTree)
                                                }
                                        )
                                )
                        )
                        .then(
                            Commands.literal("tag")
                                .then(
                                    Commands.argument("tag_id", StringArgumentType.word())
                                        .then(
                                            Commands.argument("skill_tree", StringArgumentType.word())
                                                .suggests(SKILL_TREE_SUGGESTIONS)
                                                .executes { ctx ->
                                                    val tagId = StringArgumentType.getString(ctx, "tag_id")
                                                    val skillTree = StringArgumentType.getString(ctx, "skill_tree")
                                                    executeSetTag(ctx, tagId, skillTree)
                                                }
                                        )
                                )
                        )
                )

                // Remove item override
                .then(
                    Commands.literal("remove")
                        .then(
                            Commands.literal("item")
                                .then(
                                    Commands.argument("item_id", ResourceLocationArgument.id())
                                        .suggests(ITEM_SUGGESTIONS)
                                        .executes { ctx ->
                                            val itemId = ResourceLocationArgument.getId(ctx, "item_id")
                                            executeRemoveItem(ctx, itemId)
                                        }
                                )
                        )
                        .then(
                            Commands.literal("tag")
                                .then(
                                    Commands.argument("tag_id", StringArgumentType.word())
                                        .executes { ctx ->
                                            val tagId = StringArgumentType.getString(ctx, "tag_id")
                                            executeRemoveTag(ctx, tagId)
                                        }
                                )
                        )
                )

                // List overrides
                .then(
                    Commands.literal("list")
                        .executes { ctx -> executeList(ctx) }
                )

                // Clear overrides
                .then(
                    Commands.literal("clear")
                        .executes { ctx -> executeClear(ctx) }
                )
        )
    }

    private fun parseSkillTreeId(skillTree: String): ResourceLocation {
        return when {
            skillTree.contains(":") -> ResourceLocation.parse(skillTree)
            else -> ResourceLocation.fromNamespaceAndPath(SpiritOfFight.MOD_ID, skillTree)
        }
    }

    /**
     * Expands a skill tree name to all matching skill trees.
     * If the name matches an existing skill tree exactly, returns that.
     * Otherwise, finds all skill trees that start with the name followed by ".".
     * E.g., "spear" -> [spear.block, spear.combo, spear.special_attack, ...]
     */
    private fun expandSkillTrees(source: CommandSourceStack, baseSkillTree: String): List<ResourceLocation> {
        val registry = source.server.registryAccess().registry(SOFRegistries.SKILL_TREE).orElse(null)
            ?: return emptyList()

        val baseId = parseSkillTreeId(baseSkillTree)

        // Check if the exact skill tree exists
        if (registry.get(baseId) != null) {
            return listOf(baseId)
        }

        // Find all skill trees that start with the base path
        val prefix = "${baseId.path}."
        val expanded = registry.keySet()
            .filter { it.namespace == baseId.namespace && it.path.startsWith(prefix) }
            .map { ResourceLocation.fromNamespaceAndPath(it.namespace, it.path) }
            .toList()

        return expanded
    }

    private fun executeSetItem(ctx: CommandContext<CommandSourceStack>, itemId: ResourceLocation, skillTree: String): Int {
        val source = ctx.source

        try {
            val skillTreeIds = expandSkillTrees(source, skillTree)

            if (skillTreeIds.isEmpty()) {
                source.sendFailure(Component.literal("Skill tree '$skillTree' not found. Available: sword, spear, axe, gloves, hammer, greatsword"))
                return 0
            }

            // Get item from registry
            val item = BuiltInRegistries.ITEM.get(itemId)

            if (item == null) {
                source.sendFailure(Component.literal("Item '$itemId' not found"))
                return 0
            }

            WeaponSkillOverrideManager.setItemOverride(item, skillTreeIds)

            val skillTreeStr = if (skillTreeIds.size > 1) {
                "[${skillTreeIds.joinToString(", ") { it.path }}]"
            } else {
                skillTreeIds.first().toString()
            }
            source.sendSuccess({ Component.literal("Set skill(s) $skillTreeStr for item: $itemId") }, true)
            return 1
        } catch (e: Exception) {
            source.sendFailure(Component.literal("Error: ${e.message}"))
            return 0
        }
    }

    private fun executeSetTag(ctx: CommandContext<CommandSourceStack>, tagId: String, skillTree: String): Int {
        val source = ctx.source

        try {
            val skillTreeIds = expandSkillTrees(source, skillTree)

            if (skillTreeIds.isEmpty()) {
                source.sendFailure(Component.literal("Skill tree '$skillTree' not found. Available: sword, spear, axe, gloves, hammer, greatsword"))
                return 0
            }

            // Parse tag ID
            val fullTagId = if (tagId.startsWith("#")) tagId.substring(1) else tagId
            val tagResourceId = ResourceLocation.parse(fullTagId)

            WeaponSkillOverrideManager.setTagOverride(tagResourceId, skillTreeIds)

            val skillTreeStr = if (skillTreeIds.size > 1) {
                "[${skillTreeIds.joinToString(", ") { it.path }}]"
            } else {
                skillTreeIds.first().toString()
            }
            source.sendSuccess({ Component.literal("Set skill(s) $skillTreeStr for tag: #$fullTagId") }, true)
            return 1
        } catch (e: Exception) {
            source.sendFailure(Component.literal("Error: ${e.message}"))
            return 0
        }
    }

    private fun executeRemoveItem(ctx: CommandContext<CommandSourceStack>, itemId: ResourceLocation): Int {
        val source = ctx.source

        try {
            val item = BuiltInRegistries.ITEM.get(itemId)

            if (item != null) {
                WeaponSkillOverrideManager.removeItemOverride(item)
            }
            source.sendSuccess({ Component.literal("Removed override for: $itemId") }, true)
            return 1
        } catch (e: Exception) {
            source.sendFailure(Component.literal("Error: ${e.message}"))
            return 0
        }
    }

    private fun executeRemoveTag(ctx: CommandContext<CommandSourceStack>, tagId: String): Int {
        val source = ctx.source

        try {
            val fullTagId = if (tagId.startsWith("#")) tagId.substring(1) else tagId
            val tagResourceId = ResourceLocation.parse(fullTagId)

            WeaponSkillOverrideManager.removeTagOverride(tagResourceId)
            source.sendSuccess({ Component.literal("Removed override for tag: #$fullTagId") }, true)
            return 1
        } catch (e: Exception) {
            source.sendFailure(Component.literal("Error: ${e.message}"))
            return 0
        }
    }

    private fun executeList(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        val overrides = WeaponSkillOverrideManager.getAllOverrides()

        if (overrides.isEmpty()) {
            source.sendSuccess({ Component.literal("No weapon overrides configured") }, false)
        } else {
            source.sendSuccess({ Component.literal("Weapon Overrides (${overrides.size}):") }, false)
            overrides.forEach { (target, skillTrees) ->
                val skillTreeStr = if (skillTrees.size > 1) {
                    "[${skillTrees.joinToString(", ") { it.path }}]"
                } else {
                    skillTrees.first().toString()
                }
                source.sendSuccess({ Component.literal("  $target -> $skillTreeStr") }, false)
            }
        }
        return 1
    }

    private fun executeClear(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        WeaponSkillOverrideManager.clearAll()
        source.sendSuccess({ Component.literal("Cleared all weapon overrides") }, true)
        return 1
    }
}
