Skill.create("spirit_of_fight:gloves.special.combo_3", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", false)
        config.set("enable_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.5)
        config.set("damage_multiplier", 1.5)
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = animatable.createAnimation('minecraft:player', name)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'leftLeg', [1.5, 1.5, 1.5], [0.0, -0.5, 0.0])
        anim.setShouldTurnBody(true)
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
                entity.sofCommonAttack(target, "heavy_swipe", 100, 25)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:soft_under_attack_4", "players", 1, 1)
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

        var position = null;
        const kfs = anim.registerKeyframeRanges("attack", [[0.25, 0.45], [0.3, 0.4]], (attackKF, index) => {
            attackKF.onEnter(() => {
                if (index == 1) entity.summonSpaceWarp("leftLeg", [0.0, -0.5, 0.0], 2, 2, 15, 1)
                else {
                    attackBody.setCollideWithGroups(1)
                    entity.move([0.0, entity.getDeltaMovement().y, 0.75], false)
                    position = entity.position()
                    entity.summonShadow(20, 0x808080)
                    level.playSound(entity.getOnPos().above(), "minecraft:entity.player.attack.strong", "players")
                    level.playSound(entity.getOnPos().above(), "spirit_of_fight:perfect_dodge", "players", 0.75, 1.5)
                    entity.setCameraLock(true)
                }
            })
            attackKF.onInside(() => {
                animatable.summonTrail(trailMesh, "leftLeg", [0.0, -0.5, 0.0], [0.0, -0.7, 0.0])
            })
            attackKF.onExit(() => {
                if (index == 0) attackBody.setCollideWithGroups(0)
            })
        })

        skill.onHurt(event => {
            if (kfs[0].isInProgress()) {
                if (position != null) {
                    level.playSound(position, "spirit_of_fight:sharp_parry_2", "players", 1, 2)
                    SOFParticlePresets.summonQuadraticParticle(level, [position.x, position.y + 1, position.z], 15, 'minecraft:firework')
                    event.setCanceled(true)
                }
            }
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.75)
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