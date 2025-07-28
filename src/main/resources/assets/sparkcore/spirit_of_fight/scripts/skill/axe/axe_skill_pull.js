Skill.create("spirit_of_fight:axe_pull", builder => {
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()

        if (entity == null || animatable == null) return

        const anim = animatable.createAnimation('minecraft:player', 'axe:skill_pull')
        anim.setShouldTurnBody(true)

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            SOFSkillHelper.grabToBone(animatable, "rightItem", SpMath.vec3(0.0, -0.7, -0.25))

            entity.getPreInput().executeIfPresent('special_attack')
        })

        skill.onLocalInputUpdate(event => {
            EntityHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.setCameraLock(false)
            entity.clearGrabs()
        })
    })
})