package cn.solarmoon.spirit_of_fight.skill.tree.condition

import cn.solarmoon.spark_core.data.SerializeHelper
import cn.solarmoon.spark_core.local_control.KeyEvent
import cn.solarmoon.spark_core.local_control.KeyMappingHelper
import cn.solarmoon.spark_core.local_control.onEvent
import cn.solarmoon.spark_core.skill.Skill
import cn.solarmoon.spirit_of_fight.entity.player.PlayerLocalController
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import org.joml.Vector2i

/**
 * @param activeTime 按键长按有效时间
 */
class KeyInputCondition(
    val keyMap: Map<String, KeyEvent>,
    val activeTime: List<Vector2i> = listOf()
): SkillTreeCondition {

    override fun test(host: Player, skill: Skill?): Boolean {
        return keyMap.all { (key, event) ->
            val key = KeyMappingHelper.get(key) ?: throw NullPointerException("不存在注册名为 $key 的键位")
            key.onEvent(event) { time ->
                (activeTime.isEmpty() || activeTime.any { time in it.x..it.y }) && !(PlayerLocalController.guardKeyConflict() && host.isUsingItem)
            }
        }
    }

    override val description: Component
        get() {
            val desc = Component.empty() // 创建一个空的 Component 作为基础
            val entries = keyMap.entries.toList() // 将 keyMap 转换为列表以便迭代
            entries.forEachIndexed { index, (key, event) ->
                // 获取按键的显示名称
                val keyDisplay = KeyMappingHelper.get(key)!!.key.displayName
                // 创建描述该按键-事件对的可翻译组件
                val eventDesc = Component.translatable("skill_tree_condition.${registryKey.namespace}.${registryKey.path}.${event.toString().lowercase()}", keyDisplay)
                // 添加连接词
                if (index > 0) {
                    if (index == entries.size - 1) {
                        // 最后一个元素前添加“和”
                        desc.append(Component.translatable("skill_tree_condition.also"))
                    } else {
                        // 中间元素前添加“，”
                        desc.append(Component.translatable("skill_tree_condition.comma"))
                    }
                }
                desc.append(eventDesc) // 添加当前按键-事件对的描述
            }
            return desc
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