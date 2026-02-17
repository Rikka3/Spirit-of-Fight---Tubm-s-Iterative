Skill.createBy("spirit_of_fight:mace.dodge", "spirit_of_fight:dodge", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig(config => {
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        const anim = SOFHelper.createAnimByDirection(animatable, "minecraft:player", `mace.dodge`, "forward")

        anim.onSwitchIn(p => {
            entity.getPreInput().lock()
        })

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            animatable.playAnimation(anim, 0)
        })

        const moveKF = anim.registerKeyframeRangeStart("move", 0.0)
        moveKF.onEnter(() => {
            entity.move([0.0, entity.getDeltaMovement().y, 1.25], true)
        })

        const inputKF = anim.registerKeyframeRangeStart("input", 0.35)
        inputKF.onEnter(() => {
            entity.setCameraLock(false)
        })
        inputKF.onInside((time) => {
            entity.getPreInput().executeExcept("dodge")
        })

        skill.onHurt(event => {
            event.setCanceled(true)
            if (skill.getTickCount() <= 10) {
                skill.perfectDodge(event)
            }
        })

        skill.onLocalInputUpdate(event => {
            SOFHelper.preventLocalInput(event)
        })

        skill.onPerfectDodge((event, times) => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:perfect_dodge", "players")
            animatable.summonShadow(20, 0x808080)
        })

        skill.onEnd(() => {
            entity.getPreInput().unlock()
        })
    })
})
