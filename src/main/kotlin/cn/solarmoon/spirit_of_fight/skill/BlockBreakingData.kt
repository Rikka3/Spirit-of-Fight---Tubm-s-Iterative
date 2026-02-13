package cn.solarmoon.spirit_of_fight.skill

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag

/**
 * Data class to track block breaking state and progress
 */
data class BlockBreakingData(
    var damage: Float = 0.0f,
    var maxDamage: Float = 20.0f, // Changed from max health to 10 hearts (20 damage)
    var isBreaking: Boolean = false
) {
    /**
     * Get the current break progress as a percentage (0-1)
     */
    fun getProgress(): Float {
        return (damage / maxDamage).coerceIn(0f, 1f)
    }

    /**
     * Add damage to the block and check if it should break
     */
    fun addDamage(amount: Float): Boolean {
        damage += amount
        return damage >= maxDamage
    }

    /**
     * Reset the break state
     */
    fun reset() {
        damage = 0.0f
        isBreaking = false
    }

    /**
     * Serialize to NBT
     */
    fun serialize(): CompoundTag {
        val nbt = CompoundTag()
        nbt.putFloat("damage", damage)
        nbt.putFloat("maxDamage", maxDamage)
        nbt.putBoolean("isBreaking", isBreaking)
        return nbt
    }

    /**
     * Deserialize from NBT
     */
    fun deserialize(nbt: CompoundTag): BlockBreakingData {
        damage = nbt.getFloat("damage")
        maxDamage = nbt.getFloat("maxDamage")
        isBreaking = nbt.getBoolean("isBreaking")
        return this
    }

    companion object {
        val CODEC: Codec<BlockBreakingData> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.FLOAT.fieldOf("damage").forGetter { it.damage },
                Codec.FLOAT.fieldOf("maxDamage").forGetter { it.maxDamage },
                Codec.BOOL.fieldOf("isBreaking").forGetter { it.isBreaking }
            ).apply(instance, ::BlockBreakingData)
        }
    }
}
