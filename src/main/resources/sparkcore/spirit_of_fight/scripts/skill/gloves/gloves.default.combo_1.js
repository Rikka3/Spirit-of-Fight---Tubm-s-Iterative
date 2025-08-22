Skill.create("spirit_of_fight:gloves.default.combo_1", builder => {
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.1)
        config.set("damage_multiplier", 1)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const anim = animatable.createAnimation('minecraft:player', name)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [1.5, 1.5, 1.5], [0.0, 0.0, 0.0])
        const attackBody2 = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'leftItem', [1.5, 1.5, 1.5], [0.0, 0.0, 0.0])
        anim.setShouldTurnBody(true)
        const globalAttackSystem = PhysicsHelper.createAttackSystem()
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFFFFFF)
        function bindAttackCollide(body) {
            body.onAttackCollide('attack', {
                preAttack: function (isFirst, attacker, target) {
                    skill.addTarget(target);
                    if (isFirst) {
                        entity.cameraShake(2, 1, 2);
                    }
                    entity.addFightSpirit(25);
                },
                doAttack: function (attacker, target) {
                    entity.sofCommonAttack(target, "light_stab", 25, 25);
                },
                postAttack: function (attacker, target) {
                    skill.removeTarget(target);
                }
            }, globalAttackSystem);
        }
        bindAttackCollide(attackBody);
        bindAttackCollide(attackBody2);

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:soft_under_attack_3", "players", 1, 1.1)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:crit')
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

        anim.registerKeyframeRanges("attack", [[0.1, 0.25], [0.35, 0.5]], (attackKF, index) => {
            attackKF.onEnter(() => {
                switch (index) {
                    case 0:
                        entity.move([0.0, entity.getDeltaMovement().y, 0.4], false)
                        attackBody.setCollideWithGroups(1)
                        level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.weak", "players")
                        break
                    case 1:
                        attackBody2.setCollideWithGroups(1)
                        level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.strong", "players")
                        break
                }
                entity.setCameraLock(true)
            })
            attackKF.onInside(() => {
                animatable.summonTrail(trailMesh, index === 0 ? "rightItem" : "leftItem", [0.0, 0.0, 0.1], [0.0, 0.0, -0.1])
            })
            attackKF.onExit(() => {
                globalAttackSystem.reset()
                attackBody.setCollideWithGroups(0)
                attackBody2.setCollideWithGroups(0)
            })
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.6)
        inputKF.onEnter(() => {
            entity.setCameraLock(false)
        })
        inputKF.onInside((time) => {
            entity.getPreInput().execute()
        })

        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.getPreInput().unlock()
            entity.setSolid(false)
            entity.setCameraLock(false)
            attackBody.remove()
            attackBody2.remove()
        })
    })
})