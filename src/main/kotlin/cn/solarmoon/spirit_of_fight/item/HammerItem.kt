package cn.solarmoon.spirit_of_fight.item

import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.Tier

class HammerItem(
    tier: Tier,
    properties: Properties
): PickaxeItem(tier, properties) {

    override fun getEnchantmentValue(): Int {
        return 14 // Similar to diamond tools, good for heavy weapons
    }
}
