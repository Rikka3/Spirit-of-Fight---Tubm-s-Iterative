package cn.solarmoon.spirit_of_fight.util

class OneTimeTrigger(
    val condition: () -> Boolean,
    val action: () -> Unit
) {

    var triggered = false

    fun trigger() {
        if (condition() && !triggered) {
            triggered = true
            action()
        }
    }

    fun reset() {
        triggered = false
    }

}