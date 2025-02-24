package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.local_control.KeyMappingHelper
import cn.solarmoon.spark_core.local_control.onEvent
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spark_core.skill.SkillHost
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.KeyMapping
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec2
import org.joml.Vector2i

class KeyInputCondition(
    val keyMap: Map<String, KeyEvent>,
    val activeTime: List<Vector2i> = listOf()
): SkillTreeCondition {

    override fun test(host: Player, skill: Skill?): Boolean {
        return keyMap.all { (key, event) ->
            val key = KeyMappingHelper.get(key) ?: throw NullPointerException("不存在注册名为 $key 的键位")
            key.onEvent(event) { time -> activeTime.isEmpty() || activeTime.any { time in it.x..it.y } }
        }
    }

    override val description: Component
        get() {
            val key = KeyMappingHelper.get(keyMap.keys.toList()[0])!!.key.displayName
            return Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}", key)
        }

    override val codec: MapCodec<out SkillTreeCondition> = CODEC

    companion object {
        val CODEC: MapCodec<KeyInputCondition> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.unboundedMap(Codec.STRING, KeyEvent.CODEC).fieldOf("map").forGetter { it.keyMap },
                SerializeHelper.VEC2I_CODEC.listOf().optionalFieldOf("active_time", listOf()).forGetter { it.activeTime }
            ).apply(it, ::KeyInputCondition)
        }
    }

}