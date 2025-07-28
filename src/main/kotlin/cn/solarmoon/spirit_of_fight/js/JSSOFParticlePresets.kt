package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.entity.getRelativeVector
import cn.solarmoon.spark_core.js.JSComponent
import cn.solarmoon.spark_core.util.toVec3
import cn.solarmoon.spark_core.util.ParticleUtil

import cn.solarmoon.spirit_of_fight.particle.*
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.math.Vector3f
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object JSSOFParticlePresets: JSComponent() {

    fun summonQuadraticParticle(source: DamageSource, amount: Int, particle: String) {
        summonQuadraticParticle(source, amount, particle, "")
    }

    fun summonQuadraticParticle(source: DamageSource, amount: Int, particle: String, reader: String) {
        val extraData = source.extraData ?: return
        val attacker = source.entity ?: return
        val level = attacker.level()
        summonQuadraticParticle(level, Vector3f().apply { ManifoldPoints.getPositionWorldOnA(extraData.manifoldId, this) }.toVec3(), amount, particle, reader)
    }

    fun summonQuadraticParticle(level: Level, pos: Vec3, amount: Int, particle: String) {
        summonQuadraticParticle(level, pos, amount, particle, "")
    }

    fun summonGaleParticle(level: Level, entity: Entity, amount: Int, speed: Double, offset: Vec3, particle: String) {
        summonGaleParticle(level, entity, amount, speed, offset, particle, "")
    }

    fun summonChargingParticle(level: Level, center: Vec3, amount: Int, radius: Double, still: Boolean, particle: String) {
        summonChargingParticle(level, center, amount, radius, still, particle, "")
    }

    fun summonQuadraticParticle(level: Level, pos: Vec3, amount: Int, particle: String, reader: String) {
        if (level.isClientSide) return
        val presets = ParticlePresets(level, true)
        val particle = ParticleUtil.createParticleByString(level.registryAccess(), particle, reader)
        presets.summonQuadraticParticle(pos, amount, particle)
    }

    fun summonGaleParticle(level: Level, entity: Entity, amount: Int, speed: Double, offset: Vec3, particle: String, reader: String) {
        val presets = ParticlePresets(level, true)
        val particle = ParticleUtil.createParticleByString(level.registryAccess(), particle, reader)
        presets.summonGaleParticle(entity, amount, speed, offset, particle)
    }

    fun summonChargingParticle(level: Level, center: Vec3, amount: Int, radius: Double, still: Boolean, particle: String, reader: String) {
        val presets = ParticlePresets(level, true)
        val particle = ParticleUtil.createParticleByString(level.registryAccess(), particle, reader)
        presets.summonChargingParticle(center, amount, radius, still, particle)
    }

}