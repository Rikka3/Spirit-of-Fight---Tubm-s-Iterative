package cn.solarmoon.spirit_of_fight.registry.common

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import cn.solarmoon.spirit_of_fight.item.GlovesItem
import cn.solarmoon.spirit_of_fight.item.HammerItem
import cn.solarmoon.spirit_of_fight.item.SpearItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tiers

object SOFItems {
    @JvmStatic
    fun register() {}

    @JvmStatic
    val LEATHER_GLOVES = SpiritOfFight.REGISTER.item<GlovesItem>()
        .id("leather_gloves")
        .bound { GlovesItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.WOOD, 2f, -2.2f)).durability(1024)) }
        .build()

    @JvmStatic
    val IRON_GLOVES = SpiritOfFight.REGISTER.item<GlovesItem>()
        .id("iron_gloves")
        .bound { GlovesItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.IRON, 2f, -2.1f)).durability(1024)) }
        .build()

    @JvmStatic
    val GOLDEN_GLOVES = SpiritOfFight.REGISTER.item<GlovesItem>()
        .id("golden_gloves")
        .bound { GlovesItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.GOLD, 2f, -2f)).durability(1024)) }
        .build()

    @JvmStatic
    val DIAMOND_GLOVES = SpiritOfFight.REGISTER.item<GlovesItem>()
        .id("diamond_gloves")
        .bound { GlovesItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.DIAMOND, 2f, -2f)).durability(1024)) }
        .build()

    @JvmStatic
    val NETHERITE_GLOVES = SpiritOfFight.REGISTER.item<GlovesItem>()
        .id("netherite_gloves")
        .bound { GlovesItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.NETHERITE, 2f, -2f)).durability(1024)) }
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

    @JvmStatic
    val WOODEN_SPEAR = SpiritOfFight.REGISTER.item<SpearItem>()
        .id("wooden_spear")
        .bound { SpearItem(Tiers.WOOD, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.WOOD, 5f, -3.2f)).durability(64)) }
        .build()

    @JvmStatic
    val STONE_SPEAR = SpiritOfFight.REGISTER.item<SpearItem>()
        .id("stone_spear")
        .bound { SpearItem(Tiers.STONE, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.STONE, 5f, -3.0f)).durability(512)) }
        .build()

    @JvmStatic
    val IRON_SPEAR = SpiritOfFight.REGISTER.item<SpearItem>()
        .id("iron_spear")
        .bound { SpearItem(Tiers.IRON, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.IRON, 5f, -2.9f)).durability(1024)) }
        .build()

    @JvmStatic
    val GOLDEN_SPEAR = SpiritOfFight.REGISTER.item<SpearItem>()
        .id("golden_spear")
        .bound { SpearItem(Tiers.GOLD, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.GOLD, 5f, -2.8f)).durability(128)) }
        .build()

    @JvmStatic
    val DIAMOND_SPEAR = SpiritOfFight.REGISTER.item<SpearItem>()
        .id("diamond_spear")
        .bound { SpearItem(Tiers.DIAMOND, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.DIAMOND, 5f, -2.8f)).durability(2048)) }
        .build()

    @JvmStatic
    val NETHERITE_SPEAR = SpiritOfFight.REGISTER.item<SpearItem>()
        .id("netherite_spear")
        .bound { SpearItem(Tiers.NETHERITE, Item.Properties().attributes(DiggerItem.createAttributes(Tiers.NETHERITE, 5f, -2.8f)).durability(4096)) }
        .build()

}