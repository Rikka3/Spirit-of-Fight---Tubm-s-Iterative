package cn.solarmoon.spirit_of_fight.item

import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier

class SpearItem(
    tier: Tier,
    properties: Properties
): SwordItem(tier, properties) {

    override fun getEnchantmentValue(): Int {
        return 15 // Similar to iron tools, good for weapons
    }
}