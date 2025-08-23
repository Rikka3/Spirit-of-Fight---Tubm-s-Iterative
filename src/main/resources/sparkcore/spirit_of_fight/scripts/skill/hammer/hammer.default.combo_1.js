Skill.create("spirit_of_fight:hammer.default.combo_1", builder => {
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

        const anim = animatable.createAnimation("minecraft:player", name)
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [2.0, 2.0, 2.0], [0.0, 0.0, -1.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFFFFFF)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                    entity.changeSpeed(5, 0.05)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "heavy_chop", 50, 50)
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

        const attackKF = anim.registerKeyframeRange("attack", 0.5, 0.65)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.move([0.0, entity.getDeltaMovement().y, 0.3], false)
            if (skill.isActivated()) entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_wield_1", "players")
        })
        attackKF.onInside(() => {
            animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.9], [0.0, 0.0, -1.1])
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
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
            entity.setCameraLock(false)
            attackBody.remove()
            anim.cancel()
        })
    })
})