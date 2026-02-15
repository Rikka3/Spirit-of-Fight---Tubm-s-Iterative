Skill.create("spirit_of_fight:axe.special_attack", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 1.5)
        config.set("damage_multiplier", 5.0)
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = animatable.createAnimation("minecraft:player", "axe.skill")
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [2.0, 2.0, 2.0], [0.0, 0.0, -0.75])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFF8000)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(7, 3.0, 7)
                }
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "heavy_spin", 200, 0)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_5", "players")
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 50, 'minecraft:block', '{"block_state": {"Name": "minecraft:obsidian"}}')
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 30, 'minecraft:lava')
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

        const attackKF = anim.registerKeyframeRange("attack", 0.15, 0.35)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 0.4)
        })
        attackKF.onInside(() => {
            animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.4], [0.0, 0.0, -1.5])
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
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