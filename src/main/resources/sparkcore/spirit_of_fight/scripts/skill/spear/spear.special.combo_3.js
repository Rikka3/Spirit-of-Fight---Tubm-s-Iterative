Skill.create("spirit_of_fight:spear.special.combo_3", builder => {
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.25)
        config.set("damage_multiplier", 0.5)
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
        const trailMesh = SOFHelper.createTrailMesh("spirit_of_fight:textures/particle/base_trail.png", 5, 0xFFFFFF)

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
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

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players", 1, 1.1)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
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

        anim.registerKeyframeRanges("attack", [[0.45, 0.75], [0.75, 0.95], [1.0, 1.2]], (kf, index) => {
            kf.onEnter(() => {
                attackBody.setCollideWithGroups(1)
                if (index == 2) {
                    skill.getConfig().set("damage_multiplier", 1.25)
                    entity.move([0.0, entity.getDeltaMovement().y, 0.5], false)
                }
                entity.setCameraLock(true)
                level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 1)
            })
            kf.onInside(() => {
                if (index != 2) entity.move([0.0, entity.getDeltaMovement().y, 0.25], false)
                animatable.summonTrail(trailMesh, "rightItem", [0.0, 0.0, -0.9], [0.0, 0.0, -1.1])
            })
            kf.onExit(() => {
                attackBody.setCollideWithGroups(0)
                globalAttackSystem.reset()
            })
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 1.1)
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
        })
    })
})