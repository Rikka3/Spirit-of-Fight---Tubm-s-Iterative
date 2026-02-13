Skill.create("spirit_of_fight:sword_jump_attack", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
        config.set("enable_critical_hit", true)
        config.set("enable_sweep_attack", true)
        config.set("ignore_attack_speed", true)
        config.set("target_knockback_strength", 0.5)
        config.set("damage_multiplier", 1.2)
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        // Using mace:attack_jump as placeholder for sword jump attack
        const anim = animatable.createAnimation('minecraft:player', 'mace.attack_jump')
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [2.5, 2.5, 2.5], [0.0, 0.0, -1.0])
        const globalAttackSystem = PhysicsHelper.createAttackSystem()

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                entity.addFightSpirit(30)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.sofCommonAttack(target, "heavy_chop", 60, 60)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_under_attack_1", "players")
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

        // Based on mace:attack_jump length 0.875
        const attackKF = anim.registerKeyframeRange("attack", 0.25, 0.5)
        attackKF.onEnter(() => {
            attackBody.setCollideWithGroups(1)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:hard_wield_1", "players")
        })
        attackKF.onExit(() => {
            attackBody.setCollideWithGroups(0)
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.6)
        inputKF.onInside((time) => {
            entity.getPreInput().execute()
        })

        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.getPreInput().unlock()
            entity.setSolid(false)
            attackBody.remove()
            anim.cancel()
        })
    })
})