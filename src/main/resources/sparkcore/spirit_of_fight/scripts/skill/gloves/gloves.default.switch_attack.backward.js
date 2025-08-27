Skill.create("spirit_of_fight:gloves.default.switch_attack.backward", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.5)
        config.set("damage_multiplier", 1)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = animatable.createAnimation('minecraft:player', name)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'leftLeg', [1.5, 1.5, 1.5], [0.0, -0.75, 0.0])
        anim.setShouldTurnBody(true)
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFFFFFF)
        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "heavy_upstroke", 25, 25)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:soft_under_attack_3", "players", 1, 1)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:crit')
        })

        anim.onSwitchIn(p => {
            entity.toggleWieldStyle()
            entity.getPreInput().lock()
            entity.setSolid(true)
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        anim.registerKeyframeRanges("move", [[0.0, 0.1], [0.65, 0.75]], (moveKF, index) => {
            moveKF.onEnter(() => {
                switch(index) {
                    case 0:
                        entity.move([0.0, entity.getDeltaMovement().y, -0.35], false)
                        break
                    case 1:
                        entity.move([0.0, entity.getDeltaMovement().y, -0.5], false)
                        break
                }
            })
        })
        anim.registerKeyframeRanges("attack", [[0.4, 0.55]], (attackKF, index) => {
            attackKF.onEnter(() => {
                attackBody.setCollideWithGroups(1)
                level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.weak", "players")
                entity.setCameraLock(true)
            })
            attackKF.onInside(() => {
                animatable.summonTrail(trailMesh, "leftLeg", [0.0, -0.5, 0.0], [0.0, -0.7, 0.0])
            })
            attackKF.onExit(() => {
                attackBody.setCollideWithGroups(0)
            })
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.9)
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