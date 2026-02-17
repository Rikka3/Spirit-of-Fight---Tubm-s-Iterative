Skill.create("spirit_of_fight:hammer.default.combo_3", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 1.0)
        config.set("damage_multiplier", 1.5)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = animatable.createAnimation("minecraft:player", name)
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [3.0, 3.0, 3.0], [0.0, 0.0, -1.5])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0x800000)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "knockdown_chop", 100, 100)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_under_attack_1", "players")
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

        anim.registerKeyframeRanges("move", [[0.2, 0.7], [0.7, 0.75], [0.75, 1.2], [1.1, 1.25]], (kf, index) => {
            switch(index) {
                case 0:
                    kf.onInside(() => { entity.move([0.0, 0.0, 0.1], false) })
                    break
                case 1:
                    kf.onEnter(() => {
                        entity.setCameraLock(true)
                        entity.move([0.0, 0.25, 1.0], false)
                    })
                    break
                case 2:
                    kf.onInside(() => { entity.move([0.0, 0.0, 0.25], false) })
                    break
                case 3:
                    kf.onEnter(() => { level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_wield_1", "players", 1.0, 0.9) })
                    break
            }
        })

        const attackKF = anim.registerKeyframeRange("attack", 1.3, 1.4)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.cameraShake(3, 2, 3, 15)
            entity.summonSpaceWarp("rightItem", [0.0, 0.0, -1.0], 2, 2, 15, 1)
            SOFParticlePresets.summonSeismicWave(level, entity, 40, 0.25, [0.0, 0.0, 1.0], 'minecraft:campfire_cosy_smoke')
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_under_attack_1", "players")
        })
        attackKF.onInside(() => {
            animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.9], [0.0, 0.0, -1.1])
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 1.65)
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
