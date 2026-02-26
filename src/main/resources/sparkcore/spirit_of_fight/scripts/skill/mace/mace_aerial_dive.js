Skill.create("spirit_of_fight:mace_aerial_dive", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", true)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.5)
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()
        
        if (entity == null || animatable == null) return
        
        // Get target from entity patch
        const targetId = entity.getAerialDiveTarget()
        const diveDirection = entity.getAerialDiveDirection()
        
        if (targetId == null || diveDirection == null) {
            skill.end()
            return
        }
        
        const target = level.getEntity(targetId)
        const fallDistance = entity.getFallDistance()
        
        // Create animation - uses sprint attack animation
        const anim = animatable.createAnimation('minecraft:player', 'mace.attack_sprinting')
        anim.setShouldTurnBody(false) // Don't turn body - direction is fixed
        
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [3.0, 3.0, 3.0], [0.0, 0.0, -1.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        
        // Track if we hit the primary target directly
        var directHit = false
        var primaryTargetBlocked = false
        
        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                if (isFirst) {
                    entity.cameraShake(5, 1, 2)
                }
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                // Calculate distance from primary target for AOE falloff
                const primaryTarget = level.getEntity(targetId)
                var distanceFromCenter = 0.0
                if (primaryTarget != null) {
                    distanceFromCenter = target.distanceTo(primaryTarget)
                }
                
                // Base damage from fall distance (vanilla mace scaling)
                // Vanilla: base damage + bonus per block fallen (max 20 blocks worth)
                var baseDamage = 6.0 + Math.min(fallDistance * 0.5, 20.0)
                
                // Density enchantment bonus: +0.5 damage per block fallen per level
                const mainHand = entity.getMainHandItem()
                const densityLevel = mainHand.getEnchantmentLevel(level, "minecraft:density")
                if (densityLevel > 0) {
                    baseDamage += fallDistance * 0.5 * densityLevel
                }
                
                // AOE falloff: 60% reduction per block from center
                // Damage multiplier: 1.0 at center, 0.4 at 1 block, 0.16 at 2 blocks, etc.
                const aoeMultiplier = Math.pow(0.4, distanceFromCenter)
                var finalDamage = baseDamage * aoeMultiplier
                
                // Check if target is blocking
                var isBlocked = false
                if (target.isBlocking()) {
                    isBlocked = true
                    finalDamage *= 0.3 // 30% damage when blocked
                }
                
                // Check if this is the primary target
                if (target.getId() == targetId) {
                    if (isBlocked) {
                        primaryTargetBlocked = true
                    } else {
                        directHit = true
                    }
                }
                
                // Apply damage
                target.hurt(entity.damageSources().playerAttack(entity), finalDamage)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)
        
        // Store fall distance for windburst calculation
        const storedFallDistance = fallDistance
        
        anim.onSwitchIn(p => {
            entity.getPreInput().lock()
            entity.setSolid(true)
            entity.setAerialDiving(true) // Prevent fall damage
        })
        
        anim.onEnd(event => {
            skill.end()
        })
        
        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })
        
        // Dash keyframe - dive towards target with fixed direction using entity.move()
        // Based on mace.attack_sprinting length 1.625
        const dashKF = anim.registerKeyframeRange("dash", 0.0, 0.3)
        dashKF.onInside(() => {
            // Move towards target direction - use entity.move() for proper movement
            // Direction is normalized, so we move at speed 1.5 blocks per tick
            entity.move([diveDirection.x() * 1.5, diveDirection.y() * 1.5, diveDirection.z() * 1.5], false)
        })
        
        // Attack keyframe - deal damage
        const attackKF = anim.registerKeyframeRange("attack", 0.2, 0.5)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:heavy_wield_1", "players")
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
        })
        
        // Impact keyframe - handle windburst enchantment
        const impactKF = anim.registerKeyframeRange("impact", 0.5, 0.6)
        impactKF.onEnter(() => {
            // Windburst enchantment - launch player up on direct hit (not blocked)
            if (directHit && !primaryTargetBlocked) {
                const mainHand = entity.getMainHandItem()
                const windburstLevel = mainHand.getEnchantmentLevel(level, "minecraft:wind_burst")
                if (windburstLevel > 0) {
                    // Launch player up: 0.5 + 0.3 per level (vanilla-like scaling)
                    const upVelocity = 0.5 + 0.3 * windburstLevel
                    entity.setDeltaMovement(0, upVelocity, 0)
                    entity.hurtMarked = true
                    
                    // Play windburst effect
                    level.playSound(entity.getOnPos().above(), "minecraft:entity.wind_charge.wind_burst", "players")
                    
                    // Visual effect - crit particles
                    SOFParticlePresets.summonQuadraticParticle(entity, 20, 'minecraft:crit')
                }
            }
        })
        
        const inputKF = anim.registerKeyframeRangeStart("input", 0.75)
        inputKF.onEnter(() => {
            entity.setCameraLock(false)
        })
        inputKF.onInside((time) => {
            entity.getPreInput().execute()
        })
        
        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })
        
        skill.onEnd(() => {
            entity.getPreInput().unlock()
            entity.setSolid(false)
            entity.setAerialDiving(false)
            entity.setAerialDiveTarget(null)
            entity.setAerialDiveDirection(null)
            entity.setCameraLock(false)
            entity.fallDistance = 0 // Clear fall distance to prevent fall damage
            attackBody.remove()
            anim.cancel()
        })
    })
})
