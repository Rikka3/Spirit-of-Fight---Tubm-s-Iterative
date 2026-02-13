package cn.solarmoon.spirit_of_fight.registry.client

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import com.mojang.blaze3d.platform.InputConstants
import org.lwjgl.glfw.GLFW

object SOFKeyMappings {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val BLOCK = SpiritOfFight.REGISTER.keyMapping()
        .id("block")
        .bound(GLFW.GLFW_KEY_R)
        .build()

    @JvmStatic
    val DODGE = SpiritOfFight.REGISTER.keyMapping()
        .id("dodge")
        .bound(GLFW.GLFW_KEY_LEFT_ALT)
        .build()

    @JvmStatic
    val SWITCH_POSTURE = SpiritOfFight.REGISTER.keyMapping()
        .id("switch_posture")
        .bound(GLFW.GLFW_KEY_X)
        .build()

    @JvmStatic
    val SPECIAL_ATTACK = SpiritOfFight.REGISTER.keyMapping()
        .id("special_attack")
        .bound(GLFW.GLFW_KEY_V)
        .build()

    @JvmStatic
    val OPEN_SKILL_TREE = SpiritOfFight.REGISTER.keyMapping()
        .id("open_skill_tree")
        .bound(GLFW.GLFW_KEY_K)
        .build()

    @JvmStatic
    val LOCK_ON = SpiritOfFight.REGISTER.keyMapping()
        .id("lock_on")
        .bound(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)
        .type(InputConstants.Type.MOUSE)
        .build()

}