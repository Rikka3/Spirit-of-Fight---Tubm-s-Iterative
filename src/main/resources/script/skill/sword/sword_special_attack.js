Skill.create("spirit_of_fight:sword_special_attack", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const config = skill.getConfig()
        config.disableCriticalHit()
        config.disableSweepAttack()
        config.ignoreAttackSpeed()
        config.setDamageMultiplier(1.5)

        const keepAnim = animatable.createAnimation('sword:skill_keeping')
        const attackAnim = animatable.createAnimation('sword:skill_hit')
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -1.0))
        const guardBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -1.0), body => {
            body.setCollideWithGroups(1)
        })

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId) => {
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
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
            event.getEntity().addRelativeMovement(entity.position(), SpMath.vec3(0.1, 1.0, 0.1))
            level.playSound(entity.getOnPos().above(), "minecraft:entity.arrow.hit", "players")
        })

        keepAnim.onSwitchOut(next => {
            if (next == null || next.getName() != attackAnim.getName()) skill.end()
        })
        keepAnim.onCompleted(() => {
            animatable.playAnimation(attackAnim, 0)
        })
        keepAnim.onEnd(event => {
            guardBody.remove()
        })
        attackAnim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(keepAnim, 10)
        })

        skill.onActive(() => {
            const animTime = attackAnim.getTime()
            if (!attackAnim.isCancelled()) {
                if (animTime >= 0.0 && animTime <= 0.3) {
                    entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.4), false)
                }
    
                if (animTime >= 0.25 && animTime <= 0.55) {
                    attackBody.setCollideWithGroups(1)
                } else {
                    attackBody.setCollideWithGroups(0)
                }
            }

            const preInput = entity.getPreInput()
            if (!keepAnim.isCancelled() || animTime >= 0.4) {
                preInput.executeIfPresent('special_attack')
            }
        })

        skill.onHurt(event => {
            if (!keepAnim.isCancelled()) {
                SOFSkillHelper.guard(event.getSource(), guardBody, (dBody, hitPos) => {
                    event.setCanceled(true)
                })
            }
        })

        skill.onLocalInputUpdate(event => {
            if (event.getInput().down && !attackAnim.isCancelled()) event.getEntity().setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0)
            EntityHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.setCameraLock(false)
            attackBody.remove()
        })
    })
})