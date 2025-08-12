Skill.create("spirit_of_fight:spear.default.combo_1", builder => {
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

        const anim = animatable.createAnimation("minecraft:player", name)
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [1.0, 1.0, 4.0], [0.0, 0.0, 0.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("minecraft:textures/block/stone.png", 5, 0xFFFFFF)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                }
                entity.addFightSpirit(50)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        attackBody.onCollisionActive(() => {
            entity.move([0.0, entity.getDeltaMovement().y, 0.3], false)
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 1.1)
        })

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players", 1, 1.1)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        var first = true
        skill.onActive(() => {
            const animTime = anim.getTime()

            if (animTime >= 0.3 && animTime <= 0.6) {
                animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.5], [0.0, 0.0, -1.0])
                attackBody.setCollideWithGroups(1)
            } else {
                attackBody.setCollideWithGroups(0)
            }

            if (animTime >= 1.0) {
                entity.getPreInput().execute()
            }
        })

        skill.onLocalInputUpdate(event => {
            if (event.getInput().down) event.getEntity().setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0)
            SOFHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.setCameraLock(false)
            attackBody.remove()
        })
    })
})