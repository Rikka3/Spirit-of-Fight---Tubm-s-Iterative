Skill.create("spirit_of_fight:sword.special.shield.combo_3", builder => {
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

        const anim = animatable.createAnimation("minecraft:player", name)
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [1.0, 1.0, 2.0], [0.0, 0.0, -0.75])
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
                entity.sofCommonAttack(target, "light_chop", 25, 25)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players", 1, 1.1)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
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

        const attackKF = anim.registerKeyframeRange("attack", 0.2, 0.4)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.move([0.0, entity.getDeltaMovement().y, 0.5], false)
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 0.9)
        })
        attackKF.onInside(() => {
            animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.4], [0.0, 0.0, -0.9])
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.6)
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