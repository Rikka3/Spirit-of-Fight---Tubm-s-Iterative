Skill.create("spirit_of_fight:sword.special_attack", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 1.0)
        config.set("damage_multiplier", 3.0)
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = animatable.createAnimation('minecraft:player', "sword.special.combo_3")
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [1.5, 1.5, 3.0], [0.0, 0.0, -1.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFF0000)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(5, 2.0, 5)
                }
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "heavy_chop", 100, 0)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players", 1, 0.7)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 30, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 20, 'minecraft:firework')
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

        const moveKF = anim.registerKeyframeRangeStart("move", 0.1)
        moveKF.onEnter(() => {
            entity.move([0.0, 0.5, 1.0], false)
        })

        const attackKF = anim.registerKeyframeRange("attack", 0.45, 0.65)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 0.6)
        })
        attackKF.onInside(() => {
            animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.4], [0.0, 0.0, -1.2])
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.7)
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