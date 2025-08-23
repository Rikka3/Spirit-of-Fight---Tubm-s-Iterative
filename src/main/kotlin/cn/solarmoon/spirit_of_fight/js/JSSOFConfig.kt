package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.js.JSComponent
import cn.solarmoon.spark_core.js.getMember
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.mozilla.javascript.NativeObject
import org.mozilla.javascript.Scriptable

object JSSOFConfig: JSComponent() {

    val ENTITY_DEFAULT_POISE = mutableMapOf<ResourceLocation, Int>()
    val ITEM_STATES = mutableMapOf<TagKey<Item>, String>()

    fun apply(sp: Scriptable) {
        (sp.getMember("EntityDefaultPoiseValue") as? NativeObject)?.map { ResourceLocation.parse(it.key as String) to it.value as Int }?.let {
            ENTITY_DEFAULT_POISE.putAll(it)
        }
        (sp.getMember("HeldItemStateAnimation") as? NativeObject)?.map { TagKey.create(Registries.ITEM, ResourceLocation.parse(it.key as String)) to it.value as String }?.let {
            ITEM_STATES.putAll(it)
        }
    }

}