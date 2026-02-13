Skill.create("spirit_of_fight:axe.special.pull", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
    })
    builder.accept(skill => {
        const name = skill.getLocation().getPath()
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()

        const anim = animatable.createAnimation("minecraft:player", name)
        anim.setShouldTurnBody(true)

        anim.onSwitchIn(p => {
            entity.getPreInput().lock()
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            SOFSkillHelper.grabToBone(animatable, "rightItem", [0.0, -0.7, -0.25])
            entity.getPreInput().executeIfPresent('switch_posture')
        })

        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })

        skill.onEnd(() => {
            entity.getPreInput().unlock()
            entity.setCameraLock(false)
            entity.clearGrabs()
        })
    })
})
