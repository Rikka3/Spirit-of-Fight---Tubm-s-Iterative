package cn.solarmoon.spirit_of_fight.skill.controller

import com.mojang.serialization.Codec
import net.minecraft.network.chat.Component
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity

enum class WieldStyle(
    private val id: String
): StringRepresentable {
    SINGLE_WIELD("single_wield"), DUAL_WIELD("dual_wield");

    val translatableName = Component.translatable("wield_style.${serializedName}")

    override fun getSerializedName(): String {
        return id
    }

    companion object {
        fun switch(entity: Entity) {
            when(entity.wieldStyle) {
                SINGLE_WIELD -> entity.wieldStyle = DUAL_WIELD
                DUAL_WIELD -> entity.wieldStyle = SINGLE_WIELD
            }
        }

        val CODEC: Codec<WieldStyle> = StringRepresentable.fromEnum(::values)
    }
}