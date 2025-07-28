Skill.createBy("spirit_of_fight:sword_guard", "spirit_of_fight:guard", builder => {
    builder.acceptConfig(config => {
        config.put("anim_name", "sword:guard")
        config.put("enable_parry", true)
        config.put("hurt_move", SpMath.vec3(0.0, 0.0, 1.0))
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()
        const config = skill.getConfig()

        if (entity == null || animatable == null) return

        const guardBody = PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', SpMath.vec3(1.0, 1.0, 2.0), SpMath.vec3(0.0, 0.0, -0.75), body => {
            body.setCollideWithGroups(1)
        })
        config.put('guard_body', guardBody)

        skill.onGuardHurt((event, hitPos) => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_block", "players")
            SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:crit')
        })

        skill.onParry((event, hitPos) => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_parry_1", "players")
            SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:firework')
            SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:flame')
        })

        skill.onActive(() => {
            const parryAnim = config.get("parry_anim")
            const runTime = skill.getTickCount()
            if (parryAnim.isCancelled() && runTime >= 0 && runTime <= 10) config.put('can_parry', true)
            else config.put('can_parry', false)

            entity.getPreInput().executeExcept("move", "guard")
        })

        skill.onLocalInputUpdate(event => {
            EntityHelper.preventLocalInput(event)
        })
    })
})