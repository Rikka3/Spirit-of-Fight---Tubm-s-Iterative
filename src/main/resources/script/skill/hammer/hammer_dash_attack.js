Skill.create("spirit_of_fight:hammer_dash_attack", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const config = skill.getConfig()
        config.disableCriticalHit()
        config.disableSweepAttack()
        config.ignoreAttackSpeed()
        config.setDamageMultiplier(1)

        const anim = animatable.createAnimation('hammer:attack_sprinting')
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.25, 1.75, 1.25), SpMath.vec3(0.0, 0.0, -1.0))

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId) => {
                if (isFirst) {
                    entity.cameraShake(3, 1.5, 3)
                    animatable.changeSpeed(7, 0.05)
                }
            },
            doAttack: (attacker, target, o1, o2, manifoldId) => {
                entity.commonAttack(target)
            }
        })

        attackBody.onCollisionActive(() => {
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.sweep", "players")
        })

        SOFSkillHelper.summonQuadraticHitParticle(skill, 12, 'minecraft:crit')
        skill.onTargetActualHitPost(event => {
            level.playSound(entity.getOnPos().above(), "minecraft:entity.arrow.hit", "players")
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            const animTime = anim.getTime()
            if (animTime >= 0 && animTime <= 0.3) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.25), false)
            } else if (animTime >= 0.3 && animTime <= 0.5) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 1.0), false)
            }

            if (animTime >= 0.3 && animTime <= 0.9) {
                attackBody.setCollideWithGroups(1)
            } else {
                attackBody.setCollideWithGroups(0)
            }

            if (animTime >= 1.25) {
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