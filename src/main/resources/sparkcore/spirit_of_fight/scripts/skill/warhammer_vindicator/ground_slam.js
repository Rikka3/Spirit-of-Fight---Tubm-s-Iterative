/**
 * Ground Slam Special Attack for Warhammer Vindicator
 * AOE attack that damages all entities within radius and causes knockdown
 */
Skill.create("spirit_of_fight:warhammer_vindicator.ground_slam", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.0) // We handle knockback manually
        config.set("damage_multiplier", 1.5)
    })
    
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()
        
        // AOE parameters
        const AOE_RADIUS = 4.0
        const AOE_DAMAGE = 10.0
        const KNOCKDOWN_DURATION = 40 // ticks
        
        // Create animation - reuse hammer special attack animation
        const anim = animatable.createAnimation("minecraft:player", "hammer.special_attack")
        anim.setShouldTurnBody(false)
        
        // Ground slam collision body (large sphere at feet)
        const slamBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'root', [AOE_RADIUS * 2, 2.0, AOE_RADIUS * 2], [0.0, -1.0, 0.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        
        // Track hit entities to avoid double-hitting
        const hitEntities = new java.util.HashSet()
        
        slamBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                if (!hitEntities.contains(target)) {
                    skill.addTarget(target)
                    hitEntities.add(target)
                }
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                // Apply AOE damage
                entity.sofCommonAttack(target, "heavy_chop", 50, 50)
                
                // Apply knockdown effect
                if (target instanceof net.minecraft.world.entity.LivingEntity) {
                    target.setKnockedDown(true)
                    // Set knockdown via IEntityPatch
                    if (target instanceof cn.solarmoon.spirit_of_fight.entity.IEntityPatch) {
                        target.setKnockedDown(true)
                    }
                }
                
                // Knockback away from center
                const dx = target.getX() - entity.getX()
                const dz = target.getZ() - entity.getZ()
                const dist = Math.sqrt(dx * dx + dz * dz)
                if (dist > 0) {
                    const knockbackStrength = 0.8
                    target.setDeltaMovement(
                        target.getDeltaMovement().add(
                            (dx / dist) * knockbackStrength,
                            0.3,
                            (dz / dist) * knockbackStrength
                        )
                    )
                }
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)
        
        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "minecraft:entity.generic.explode", "blocks")
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 20, 'minecraft:explosion')
        })
        
        anim.onSwitchIn(p => {
            entity.getPreInput().lock()
            entity.setSolid(true)
            // Play slam sound
            level.playSound(entity.getOnPos().above(), "minecraft:entity.iron_golem.attack", "hostile")
        })
        
        anim.onEnd(event => {
            skill.end()
        })
        
        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })
        
        // Attack keyframe - when the slam hits the ground
        const attackKF = anim.registerKeyframeRange("attack", 0.4, 0.6)
        attackKF.onEnter(() => {
            slamBody.setCollideWithGroups(1)
            entity.move([0.0, 0.0, 0.0], false)
            
            // Screen shake for nearby players
            entity.cameraShake(4, 1, 3)
            
            // Spawn ground particles
            SOFParticlePresets.summonQuadraticParticle(entity, 30, 'minecraft:cloud')
            
            // Play impact sound
            level.playSound(entity.getOnPos().above(), "minecraft:entity.generic.explode", "blocks")
        })
        
        attackKF.onInside(() => {
            // Spawn shockwave particles
            SOFParticlePresets.summonQuadraticParticle(entity, 10, 'minecraft:crack')
        })
        
        attackKF.onExit(() => {
            slamBody.setCollideWithGroups(0)
        })
        
        // Input keyframe - when entity can act again
        const inputKF = anim.registerKeyframeRangeStart("input", 0.8)
        inputKF.onEnter(() => {
            entity.setSolid(false)
        })
        inputKF.onInside((time) => {
            entity.getPreInput().execute()
        })
        
        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })
    })
})
