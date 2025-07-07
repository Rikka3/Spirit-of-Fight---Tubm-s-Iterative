Skill.create("spirit_of_fight:hammer_combo_2", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const config = skill.getConfig()
        config.setCanCriticalHit(false)
        config.setCanSweepAttack(false)
        config.setIgnoreAttackSpeed(true)
        config.setDamageMultiplier(1.25)

        const anim = animatable.createAnimation('minecraft:player', 'hammer:combo_2')
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(2.0, 2.0, 2.0), SpMath.vec3(0.0, 0.0, -1.0))

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(3, 2, 3)
                    animatable.changeSpeed(7, 0.05)
                }
                entity.addFightSpirit(100)
            },
            doAttack: (attacker, target, o1, o2, manifoldId) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId) => {
                skill.removeTarget(target)
            }
        })

        attackBody.onCollisionActive(() => {
            entity.setCameraLock(true)
            SOFParticlePresets.summonGaleParticle(level, entity, 40, 1.0, SpMath.vec3(0.0, 0.0, 1.0), 'minecraft:campfire_cosy_smoke')
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_under_attack_1", "players")
        })

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_under_attack_2", "players")
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
            if (animTime >= 0.0 && animTime <= 0.65) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.25), false)
            }

            if (animTime >= 0.55 && animTime <= 0.65) {
                attackBody.setCollideWithGroups(1)
            } else {
                attackBody.setCollideWithGroups(0)
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