package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.command.WeaponOverrideCommand
import cn.solarmoon.spirit_of_fight.debug.SimpleDebugCommands
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandSourceStack
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.bus.api.SubscribeEvent

object SOFSimpleDebugRegister {

    @JvmStatic
    fun register() {
        // Debug system registration
    }

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        val dispatcher: CommandDispatcher<CommandSourceStack> = event.dispatcher
        SimpleDebugCommands.register(dispatcher)
        WeaponOverrideCommand.register(dispatcher)
    }
}