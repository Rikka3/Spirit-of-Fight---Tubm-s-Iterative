package cn.solarmoon.spirit_of_fight.entity

import cn.solarmoon.spark_core.event.ChangePresetAnimEvent
import cn.solarmoon.spark_core.registry.common.SparkRegistries
import cn.solarmoon.spark_core.registry.common.SparkStateMachineRegister
import cn.solarmoon.spark_core.resource.common.SparkResourcePathBuilder
import cn.solarmoon.spark_core.state_machine.presets.PlayerBaseAnimStateMachine
import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.event.OnSkillTreeSetChangeEvent
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import ru.nsk.kstatemachine.statemachine.processEventBlocking

object StateAnimApplier {

    @SubscribeEvent
    private fun transModifier(event: ChangePresetAnimEvent.PlayerState) {
        val player = event.player
        fun set(tag: TagKey<Item>, prefix: String) {
            if (player.mainHandItem.`is`(tag)) {
                val stateName = event.state.name
                val animationPath = SparkResourcePathBuilder.buildAnimationPath(SpiritOfFight.MOD_ID, SpiritOfFight.MOD_ID, "player", "state.$prefix.$stateName")
                when(stateName) {
                    "land.idle", "land.move", "land.move_back", "land.sprint" -> event.newAnim = SparkRegistries.TYPED_ANIMATION[animationPath]
                }
            }
        }

        set(ItemTags.SWORDS, "sword")
    }

    @SubscribeEvent
    private fun weaponChangeResetAnim(event: OnSkillTreeSetChangeEvent) {
        val player = event.player
        player.getStateMachineHandler(SparkStateMachineRegister.PLAYER_BASE_STATE)?.machine?.processEventBlocking(PlayerBaseAnimStateMachine.ResetEvent)
    }

}