Skill.create("spirit_of_fight:sword.switch_attack", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.25)
        config.set("damage_multiplier", 1)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()
        let hasHit = false

        const anim = animatable.createAnimation("minecraft:player", name)
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'leftItem', [1.5, 1.5, 1.5], [-0.25, 0.0, 0.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFFFFFF)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                }
                if (entity.isRecoveryStun()) {
                    entity.setRecoveryStun(false)
                    entity.setStunned(0)
                }
                hasHit = true
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "light_stab", 25, 25)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            const target = event.getEntity()
            level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.knockback", "players", 1, 0.85)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
        })

        anim.onSwitchIn(p => {
            entity.toggleWieldStyle()
            entity.getPreInput().lock()
            entity.setSolid(true)
        })

        anim.onEnd(event => {
            if (entity.isRecoveryStun() && !hasHit) {
                entity.setSwitchAttackCooldown(level.getGameTime() + 40)
                entity.setStunned(30)
            }
            skill.end()
        })

        skill.onActiveStart(() => {
            if (level.getGameTime() < entity.getSwitchAttackCooldown()) {
                skill.end()
                return
            }
            hasHit = false
            animatable.playAnimation(anim, 0)
        })

        const attackKF = anim.registerKeyframeRange("attack", 0.3, 0.45)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.move([0.0, entity.getDeltaMovement().y, 1.0], false)
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.strong", "players")
        })
        attackKF.onInside(() => {
            //animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.4], [0.0, 0.0, -0.9])
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.5)
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
            entity.setCameraLock(false)
            attackBody.remove()
        })
    })
})