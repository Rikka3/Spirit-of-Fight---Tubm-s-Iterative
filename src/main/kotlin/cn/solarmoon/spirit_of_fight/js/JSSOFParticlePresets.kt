package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.animation.IAnimatable
import cn.solarmoon.spark_core.animation.IEntityAnimatable
import cn.solarmoon.spark_core.entity.attack.SparkHurtDatas
import cn.solarmoon.spark_core.js.JSComponent
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.util.ParticleUtil
import cn.solarmoon.spirit_of_fight.particle.*
import com.jme3.bullet.collision.ManifoldPoints
import com.jme3.math.Vector3f
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import org.mozilla.javascript.NativeArray

object JSSOFParticlePresets: JSComponent() {

    fun summonQuadraticParticle(source: DamageSource, amount: Int, particle: String) {
        summonQuadraticParticle(source, amount, particle, "")
    }

    fun summonQuadraticParticle(source: DamageSource, amount: Int, particle: String, reader: String) {
        val extraData = source.extraData.read(SparkHurtDatas.COLLISION) ?: return
        val attacker = source.entity ?: return
        val level = attacker.level()
        val pos = Vector3f().apply { ManifoldPoints.getPositionWorldOnA(extraData.manifoldId, this) }
        summonQuadraticParticle(level, NativeArray(arrayOf(pos.x, pos.y, pos.z)), amount, particle, reader)
    }

    fun summonQuadraticParticle(level: Level, pos: NativeArray, amount: Int, particle: String) {
        summonQuadraticParticle(level, pos, amount, particle, "")
    }

    fun summonSeismicWave(level: Level, entity: Entity, amount: Int, speed: Double, offset: NativeArray, particle: String) {
        summonSeismicWave(level, entity, amount, speed, offset, particle, "")
    }

    fun summonChargingParticle(level: Level, center: NativeArray, amount: Int, radius: Double, still: Boolean, particle: String) {
        summonChargingParticle(level, center, amount, radius, still, particle, "")
    }

    fun summonQuadraticParticle(level: Level, pos: NativeArray, amount: Int, particle: String, reader: String) {
        if (level.isClientSide) return
        val presets = ParticlePresets(level, true)
        val particle = ParticleUtil.createParticleByString(level.registryAccess(), particle, reader)
        presets.summonQuadraticParticle(pos.toVec3(), amount, particle)
    }

    fun summonSeismicWave(level: Level, entity: Entity, amount: Int, speed: Double, offset: NativeArray, particle: String, reader: String) {
        val presets = ParticlePresets(level, true)
        val particle = ParticleUtil.createParticleByString(level.registryAccess(), particle, reader)
        presets.summonSeismicWave(entity, amount, speed, offset.toVec3(), particle)
    }

    fun summonChargingParticle(level: Level, center: NativeArray, amount: Int, radius: Double, still: Boolean, particle: String, reader: String) {
        val presets = ParticlePresets(level, true)
        val particle = ParticleUtil.createParticleByString(level.registryAccess(), particle, reader)
        presets.summonChargingParticle(center.toVec3(), amount, radius, still, particle)
    }

    fun summonSplashFromBoneMotion(
        animatable: IAnimatable<*>,
        particle: String,
        bone: String,
        offset: NativeArray,
        radius: Float,
        strength: Float,
        rings: Int,
        forwardBias: Float,
        density: Float
    ) {
        ParticlePresets(animatable.animLevel, true).summonSplashFromBoneMotion(
            animatable,
            ParticleUtil.createParticleByString(animatable.animLevel.registryAccess(), particle),
            bone,
            offset.toVec3(),
            radius,
            strength,
            rings,
            forwardBias,
            density
        )
    }

    fun summonSplashRelateYRot(
        animatable: IEntityAnimatable<*>,
        particle: String,
        bone: String,
        relateV: NativeArray,
        offset: NativeArray,
        radius: Float,
        strength: Float,
        rings: Int,
        forwardBias: Float,
        density: Float
    ) {
        ParticlePresets(animatable.animLevel, true).summonSplashRelateYRot(
            animatable,
            ParticleUtil.createParticleByString(animatable.animLevel.registryAccess(), particle),
            bone,
            relateV.toVec3(),
            offset.toVec3(),
            radius,
            strength,
            rings,
            forwardBias,
            density
        )
    }


}