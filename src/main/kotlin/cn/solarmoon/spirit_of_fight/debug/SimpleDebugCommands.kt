package cn.solarmoon.spirit_of_fight.debug

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object SimpleDebugCommands {
    
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("sof_debug")
                .requires { source -> source.hasPermission(2) }
                .then(
                    Commands.literal("enable")
                        .executes { context ->
                            SimpleBlockDebug.setEnabled(true)
                            context.source.sendSuccess({ Component.literal("Block debugging enabled") }, true)
                            1
                        }
                )
                .then(
                    Commands.literal("disable")
                        .executes { context ->
                            SimpleBlockDebug.setEnabled(false)
                            context.source.sendSuccess({ Component.literal("Block debugging disabled") }, true)
                            1
                        }
                )
                .then(
                    Commands.literal("test")
                        .executes { context ->
                            val player = context.source.entity
                            if (player != null) {
                                SimpleBlockDebug.log("Manual test triggered")
                                SimpleBlockDebug.logState(player.id, "TEST")
                                context.source.sendSuccess({ Component.literal("Debug test triggered") }, true)
                            } else {
                                context.source.sendFailure(Component.literal("This command must be run by a player"))
                            }
                            1
                        }
                )
        )
    }
}