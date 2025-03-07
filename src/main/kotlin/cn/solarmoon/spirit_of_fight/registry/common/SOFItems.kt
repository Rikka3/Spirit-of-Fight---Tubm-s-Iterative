package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.item.GlovesItem
import cn.solarmoon.spirit_of_fight.item.HammerItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tiers

object SOFItems {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val GLOVES = SpiritOfFight.REGISTER.item<GlovesItem>()
        .id("glove")
        .bound { GlovesItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.IRON, 2f, -2f)).durability(1024)) }
        .build()

    @JvmStatic
    val WOODEN_WARHAMMER = SpiritOfFight.REGISTER.item<HammerItem>()
        .id("wooden_warhammer")
        .bound { HammerItem(Tiers.WOOD, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.WOOD, 6f, -3.2f)).durability(64)) }
        .build()

    @JvmStatic
    val STONE_WARHAMMER = SpiritOfFight.REGISTER.item<HammerItem>()
        .id("stone_warhammer")
        .bound { HammerItem(Tiers.STONE, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.STONE, 6f, -3.2f)).durability(512)) }
        .build()

    @JvmStatic
    val IRON_WARHAMMER = SpiritOfFight.REGISTER.item<HammerItem>()
        .id("iron_warhammer")
        .bound { HammerItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.IRON, 6f, -3.1f)).durability(1024)) }
        .build()

    @JvmStatic
    val GOLDEN_WARHAMMER = SpiritOfFight.REGISTER.item<HammerItem>()
        .id("golden_warhammer")
        .bound { HammerItem(Tiers.GOLD, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.GOLD, 6f, -3f)).durability(128)) }
        .build()

    @JvmStatic
    val DIAMOND_WARHAMMER = SpiritOfFight.REGISTER.item<HammerItem>()
        .id("diamond_warhammer")
        .bound { HammerItem(Tiers.DIAMOND, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.DIAMOND, 6f, -3f)).durability(2048)) }
        .build()

    @JvmStatic
    val NETHERITE_WARHAMMER = SpiritOfFight.REGISTER.item<HammerItem>()
        .id("netherite_warhammer")
        .bound { HammerItem(Tiers.NETHERITE, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.NETHERITE, 6f, -3f)).durability(4096)) }
        .build()

}