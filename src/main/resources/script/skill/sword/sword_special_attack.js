Skill.create("spirit_of_fight:sword_special_attack", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const config = skill.getConfig()
        config.setCanCriticalHit(false)
        config.setCanSweepAttack(false)
        config.setIgnoreAttackSpeed(true)
        config.setCanTargetKnockBack(false)
        config.setDamageMultiplier(1.5)

        const anim = animatable.createAnimation('minecraft:player', 'sword:skill_hit')
        const body = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -0.75))

        body.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                    animatable.changeSpeed(7, 0.05)
                }
                SOFParticlePresets.summonQuadraticParticle(level, PhysicsHelper.getContactPosA(manifoldId), 15, 'minecraft:firework')
            },
            doAttack: (attacker, target, o1, o2, manifoldId) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId) => {
                skill.removeTarget(target)
            }
        })

        body.onCollisionActive(() => {
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players")
        })

        skill.onTargetActualHurtPost(event => {
            event.getEntity().addRelativeMovement(entity.position(), SpMath.vec3(0.1, 1.0, 0.1))
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_2", "players")
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            const animTime = anim.getTime()
            if (animTime >= 0.0 && animTime <= 0.3) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.4), false)
            }

            if (animTime >= 0.25 && animTime <= 0.55) {
                body.setCollideWithGroups(1)
            } else {
                body.setCollideWithGroups(0)
            }
        })

        skill.onLocalInputUpdate(event => {
            if (event.getInput().down) event.getEntity().setDeltaMovement(0.0, entity.getDeltaMovement().y, 0.0)
            EntityHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.setCameraLock(false)
            body.remove()
        })
    })
})