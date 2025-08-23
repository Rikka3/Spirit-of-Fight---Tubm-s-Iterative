Skill.create("spirit_of_fight:gloves.default.combo_3", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.5)
        config.set("damage_multiplier", 2.0)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = animatable.createAnimation('minecraft:player', name)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'root', [3, 3, 3], [0.0, 1.75, -0.75])
        anim.setShouldTurnBody(true)
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0x800000)
        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(3, 2, 3)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "knockdown_stab", 100, 100)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:crit')
        })

        anim.onSwitchIn(p => {
            entity.getPreInput().lock()
            entity.setSolid(true)
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        anim.registerKeyframeRanges("attack", [[0.55, 0.65]], (attackKF, index) => {
            attackKF.onEnter(() => {
                attackBody.setCollideWithGroups(1)
                entity.move([0.0, entity.getDeltaMovement().y, 1.5], false)
                entity.setCameraLock(true)
                level.playSound(entity.getOnPos().above(), "spirit_of_fight:soft_under_attack_1", "players", 1, 1)
                SOFParticlePresets.summonSplashRelateYRot(entity, 'minecraft:cloud', "rightItem", [0, 0, 1], [0, 0, 0.5], 0.5, 0.5, 3, 1.5, 1)
                entity.summonSpaceWarp("rightItem", [0.0, 0.0, 0.5], 2, 5, 15, 1)
            })
            attackKF.onInside(() => {
                animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, 0.1], [0.0, 0.0, -0.1])
            })
            attackKF.onExit(() => {
                attackBody.setCollideWithGroups(0)
            })
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 1.0)
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