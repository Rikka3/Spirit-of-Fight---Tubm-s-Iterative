Skill.create("spirit_of_fight:sword_combo_2", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const config = skill.getConfig()
        config.disableCriticalHit()
        config.disableSweepAttack()
        config.ignoreAttackSpeed()
        config.setDamageMultiplier(1.25)

        const anim = animatable.createAnimation('sword:combo_2')
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -1.0))

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

        SOFSkillHelper.summonQuadraticHitParticle(skill, 12, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
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
            if (animTime >= 0.1 && animTime <= 0.35) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.5), false)
            }

            if (animTime >= 0.25 && animTime <= 0.5) {
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