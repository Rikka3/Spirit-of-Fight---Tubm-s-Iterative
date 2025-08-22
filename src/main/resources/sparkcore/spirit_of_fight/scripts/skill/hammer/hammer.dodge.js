Skill.createBy("spirit_of_fight:hammer.dodge", "spirit_of_fight:dodge", builder => {
    builder.acceptConfig(config => {
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const anim = SOFHelper.createAnimByDirection(animatable, "minecraft:player", `hammer.${entity.getWieldStyleName()}.dodge`, "forward")

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

        const inputKF = anim.registerKeyframeRangeStart("input", 0.3)
        inputKF.onEnter(() => {
            entity.setCameraLock(false)
        })
        inputKF.onInside((time) => {
            entity.getPreInput().executeExcept("dodge")
        })

        skill.onHurt(event => {
            event.setCanceled(true)
            if (skill.getTickCount() <= 5) {
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