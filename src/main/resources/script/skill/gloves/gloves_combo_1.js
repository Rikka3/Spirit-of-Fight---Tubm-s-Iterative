Skill.create("spirit_of_fight:gloves_combo_1", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const config = skill.getConfig()
        config.setCanCriticalHit(false)
        config.setCanSweepAttack(false)
        config.setIgnoreAttackSpeed(true)
        config.setDamageMultiplier(1)

        const anim = animatable.createAnimation('minecraft:player', 'baimei:combo_1')
        anim.setShouldTurnBody(true)
        const attackBodyR = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.25, 1.25, 1.25), SpMath.vec3(0.0, 0.0, 0.0))
        const attackBodyL = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'leftItem', SpMath.vec3(1.25, 1.25, 1.25), SpMath.vec3(0.0, 0.0, 0.0))

        attackBodyR.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId) => {
                skill.removeTarget(target)
            }
        })

        attackBodyL.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                }
                entity.addFightSpirit(25)
            },
            doAttack: (attacker, target, o1, o2, manifoldId) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId) => {
                skill.removeTarget(target)
            }
        })

        attackBodyR.onCollisionActive(() => {
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.weak", "players")
        })
        attackBodyL.onCollisionActive(() => {
            level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.strong", "players")
        })

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:soft_under_attack_3", "players")
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:crit')
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            const animTime = anim.getTime()
            if (animTime >= 0.05 && animTime <= 0.2) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.2), false)
            } else if (animTime >= 0.35 && animTime <= 0.5) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.45), false)
            }

            if (animTime >= 0.15 && animTime <= 0.3) {
                attackBodyR.setCollideWithGroups(1)
            } else {
                attackBodyR.setCollideWithGroups(0)
            }

            if (animTime >= 0.35 && animTime <= 0.45) {
                attackBodyL.setCollideWithGroups(1)
            } else {
                attackBodyL.setCollideWithGroups(0)
            }


            if (animTime >= 0.45) {
                entity.getPreInput().execute()
            }
        })

        skill.onLocalInputUpdate(event => {
            if (event.getInput().down) event.getEntity().setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0)
            EntityHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.setCameraLock(false)
            attackBodyR.remove()
            attackBodyL.remove()
        })
    })
})