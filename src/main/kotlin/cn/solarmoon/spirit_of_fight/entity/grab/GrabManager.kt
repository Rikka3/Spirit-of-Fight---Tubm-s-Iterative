package cn.solarmoon.spirit_of_fight.entity.grab

import net.minecraft.world.entity.Entity

class GrabManager(private val entity: Entity) {

    private val grabs = linkedSetOf<Entity>()

    var grabbedBy: Entity? = null

    fun add(grab: Entity) {
        if (grabs.add(grab)) {
            grab.grabManager.grabbedBy = entity
        }
    }

    fun addAll(grabs: Collection<Entity>) {
        grabs.forEach {
            add(it)
        }
    }

    fun remove(grab: Entity) {
        if (grabs.remove(grab) && grab.grabManager.grabbedBy == entity) {
            grab.grabManager.grabbedBy = null
        }
    }

    fun clear() {
        grabs.forEach { if (it.grabManager.grabbedBy == entity) it.grabManager.grabbedBy = null }
        grabs.clear()
    }

    fun getGrabs() = grabs.filter { it.grabManager.grabbedBy == entity }.toList()

}