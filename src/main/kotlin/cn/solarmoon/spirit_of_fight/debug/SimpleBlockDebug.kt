package cn.solarmoon.spirit_of_fight.debug

import cn.solarmoon.spirit_of_fight.SpiritOfFight
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object SimpleBlockDebug {

    private val logger: Logger = LoggerFactory.getLogger("BLOCK_DEBUG")
    private var enabled = false
    
    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        logger.info("Block debug ${if (enabled) "ENABLED" else "DISABLED"}")
    }
    
    fun log(message: String) {
        if (enabled) {
            logger.info("[BLOCK] $message")
        }
    }
    
    fun logInput(action: String, details: String = "") {
        if (enabled) {
            logger.info("[INPUT] $action${if (details.isNotEmpty()) " - $details" else ""}")
        }
    }
    
    fun logSkillTree(event: String, result: String) {
        if (enabled) {
            logger.info("[SKILL_TREE] $event -> $result")
        }
    }
    
    fun logPhysics(event: String, success: Boolean) {
        if (enabled) {
            logger.info("[PHYSICS] $event - ${if (success) "SUCCESS" else "FAILED"}")
        }
    }
    
    fun logCollision(event: String, hit: Boolean) {
        if (enabled) {
            logger.info("[COLLISION] $event - ${if (hit) "HIT" else "MISS"}")
        }
    }
    
    fun logSpirit(action: String) {
        if (enabled) {
            logger.info("[SPIRIT] $action")
        }
    }
    
    fun logAnimation(event: String, animName: String) {
        if (enabled) {
            logger.info("[ANIMATION] $event - $animName")
        }
    }
    
    fun logError(component: String, error: String) {
        if (enabled) {
            logger.error("[ERROR] $component - $error")
        }
    }
    
    fun logState(entityId: Int, state: String) {
        if (enabled) {
            logger.info("[STATE] Entity $entityId - $state")
        }
    }
}