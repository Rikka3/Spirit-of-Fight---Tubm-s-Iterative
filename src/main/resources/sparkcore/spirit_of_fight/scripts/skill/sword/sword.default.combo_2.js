Skill.create("spirit_of_fight:sword.default.combo_2", builder => {
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

        if (entity == null || animatable == null) return

        const anim = animatable.createAnimation('minecraft:player', name)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [1.0, 1.0, 2.0], [0.0, 0.0, -0.75])
        anim.setShouldTurnBody(true)
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("minecraft:textures/block/stone.png", 5, 0xFFFFFF)
        var count = 0

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                    if (count == 1) animatable.changeSpeed(5, 0.025)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        attackBody.onCollisionActive(() => {
            if (count == 0) entity.move([0.0, entity.getDeltaMovement().y, 0.4], false)
            entity.setCameraLock(true)
            globalAttackSystem.reset()
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players")
            count++
        })

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players")
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
        })

        anim.onSwitchIn(p => {
            entity.getPreInput().lock()
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            const animTime = anim.getTime()

            if ((animTime >= 0.2 && animTime <= 0.4) || (animTime >= 0.45 && animTime <= 0.65)) {
                animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.5], [0.0, 0.0, -1.0])
                attackBody.setCollideWithGroups(1)
            } else {
                attackBody.setCollideWithGroups(0)
            }

            if (animTime >= 0.7) {
                entity.getPreInput().execute()
            }
        })

        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.getPreInput().unlock()
            entity.setCameraLock(false)
            attackBody.remove()
        })
    })
})