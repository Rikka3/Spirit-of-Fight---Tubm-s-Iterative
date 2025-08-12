package cn.solarmoon.spirit_of_fight.js

import cn.solarmoon.spark_core.js.extension.JSAnimatable
import cn.solarmoon.spark_core.js.toVec3
import cn.solarmoon.spark_core.visual_effect.trail.TrailMesh
import org.mozilla.javascript.NativeArray

interface JSSOFAnimatable: JSAnimatable {

    fun summonTrail(mesh: TrailMesh, bone: String, off1: NativeArray, off2: NativeArray) {
        if (!js_animatable.animLevel.isClientSide) return
        mesh.addPoint(
            { js_animatable.getWorldBonePivot(bone, off1.toVec3(), it) },
            { js_animatable.getWorldBonePivot(bone, off2.toVec3(), it) },
        )
    }

}