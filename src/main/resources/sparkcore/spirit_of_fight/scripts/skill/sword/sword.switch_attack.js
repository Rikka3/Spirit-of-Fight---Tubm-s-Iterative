Skill.create("spirit_of_fight:sword.switch_attack", builder => {
    builder.acceptConfig(config => {
        config.set("can_critical_hit", false)
        config.set("can_sweep_attack", false)
        config.set("ignore_attack_speed", true)
        config.set("damage_multiplier", 0.25)
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const anim = animatable.createAnimation('spirit_of_fight:spirit_of_fight/animations/player/fight_skill/fightskill_sword','sword.switch_attack')
        anim.setShouldTurnBody(true)
        const attackBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'leftItem', SpMath.vec3(1.0, 1.0, 1.0), SpMath.vec3(0.0, 0.0, -0.75))
        
        // 创建全局 AttackSystem 用于管理攻击状态
        const globalAttackSystem = PhysicsHelper.createAttackSystem()

        attackBody.onAttackCollide('attack', {
            preAttack: (isFirst, attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.addTarget(target)
                if (isFirst) {
                    entity.cameraShake(2, 1, 2)
                    animatable.changeSpeed(7, 0.05)
                }
                entity.addFightSpirit(50)
            },
            doAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                entity.commonAttack(target)
            },
            postAttack: (attacker, target, o1, o2, manifoldId, attackSystem) => {
                skill.removeTarget(target)
            }
        }, globalAttackSystem)

        attackBody.onCollisionActive(() => {
            entity.setCameraLock(true)
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_wield_1", "players", 1, 1.1)
        })

        skill.onTargetActualHurtPost(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_under_attack_1", "players", 1, 1.1)
            SOFParticlePresets.summonQuadraticParticle(event.getSource(), 15, 'minecraft:block', '{"block_state": {"Name": "minecraft:redstone_block"}}')
        })
        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
            entity.toggleWieldStyle()
            // 技能开始时重置 AttackSystem 和攻击段
            currentAttackPhase = 0
            globalAttackSystem.reset()
        })

        skill.onActive(() => {
            const animTime = anim.getTime()
            if (animTime >= 0.2 && animTime <= 0.3) {
                entity.move(SpMath.vec3(0.0, entity.getDeltaMovement().y, 0.25), false)
            }

            // 攻击段管理和 AttackSystem 重置
            if (animTime >= 0.3 && animTime <= 0.45) {
                attackBody.setCollideWithGroups(1)
            } else {
                attackBody.setCollideWithGroups(0)
            }

            if (animTime >= 0.4) {
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