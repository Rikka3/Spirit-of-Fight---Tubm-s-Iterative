package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.skill.tree.SkillTree
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DataPackRegistryEvent

object SOFDataRegistryRegister {

    private fun reg(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(SOFRegistries.SKILL_TREE, SkillTree.CODEC, SkillTree.CODEC)
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}