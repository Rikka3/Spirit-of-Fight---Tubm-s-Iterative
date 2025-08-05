Skill.create("spirit_of_fight:sword.default_wield.combo_3", builder => {
    builder.acceptConfig(config => {
        config.set("can_critical_hit", false)
        config.set("can_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.25)
        config.set("damage_multiplier", 1.25)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const anim = animatable.createAnimation('spirit_of_fight:spirit_of_fight/animations/player/fight_skill/fightskill_sword', name)
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -0.75))
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("minecraft:textures/block/stone.png", 5, 0xFFFFFF)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(3, 1.5, 3)
                }
                entity.addFightSpirit(100)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        attackBody.onCollisionActive(() => {
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 0.8)
        })

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players", 1, 0.9)
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

            if (animTime >= 0.1 && first) {
                first = false
                entity.move(SpMath.vec3(0.0, 0.25, 0.5), false)
            }

            if (animTime >= 0.25 && animTime <= 0.5) {
                animatable.summonTrail(trailMesh, "rightItem", SpMath.vec3(0.0, 0.0, -0.5), SpMath.vec3(0.0, 0.0, -1.0))
                attackBody.setCollideWithGroups(1)
            } else {
                attackBody.setCollideWithGroups(0)
            }

            if (animTime >= 0.7) {
                entity.getPreInput().execute()
            }
        })

        skill.onLocalInputUpdate(event => {
            if (event.getInput().down) event.getEntity().setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0)
            EntityHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.setCameraLock(false)
            attackBody.remove()
        })
    })
})