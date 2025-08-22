package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.js.JSComponent
import cn.solarmoon.spark_core.js.getMember
import net.minecraft.resources.ResourceLocation
import org.mozilla.javascript.NativeObject
import org.mozilla.javascript.Scriptable

object JSSOFConfig: JSComponent() {

    val ENTITY_DEFAULT_POISE = mutableMapOf<ResourceLocation, Int>()

    fun apply(sp: Scriptable) {
        ENTITY_DEFAULT_POISE.putAll(
            (sp.getMember("EntityDefaultPoiseValue") as NativeObject).map { ResourceLocation.parse(it.key as String) to it.value as Int }
        )
    }

}