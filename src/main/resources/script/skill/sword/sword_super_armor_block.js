Skill.create("spirit_of_fight:sword_super_armor_block", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const keepAnim = animatable.createAnimation('minecraft:player', 'sword:skill_keeping')
        const guardBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -0.75), body => {
            body.setCollideWithGroups(1)
        })
        
        keepAnim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(keepAnim, 10)
        })

        skill.onActive(() => {
            const preInput = entity.getPreInput()
            if (skill.getTickCount() % 5 == 0) SOFParticlePresets.summonChargingParticle(level, entity.position(), 30, 2, !entity.getFightSpirit().isFull(), 'minecraft:white_smoke')
            preInput.executeIfPresent('special_attack')
        })

        skill.onHurt(event => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_block", "players")
            SOFSkillHelper.guard(event.getSource(), guardBody, (dBody, hitPos) => {
                SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:crit')
                event.setCanceled(true)
            })
        })

        skill.onLocalInputUpdate(event => {
            EntityHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            guardBody.remove()
        })
    })
})