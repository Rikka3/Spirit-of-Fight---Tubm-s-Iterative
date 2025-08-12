Skill.createBy("spirit_of_fight:sword.dodge", "spirit_of_fight:dodge", builder => {
    builder.acceptConfig(config => {
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()

        if (entity == null || animatable == null) return

        const anim = SOFHelper.createAnimByDirection(animatable, "minecraft:player", "sword.dodge", "forward")

        anim.onEnd(event => {
            skill.end()
        })

        skill.onActiveStart(() => {
            entity.getPreInput().lock()
            animatable.playAnimation(anim, 0)
        })

        skill.onActive(() => {
            const animTime = anim.getTime()

            if (animTime >= 0.0 && animTime <= 0.1) {
                entity.move([0.0, entity.getDeltaMovement().y, 1.25], true)
            }

            if (animTime >= 0.3) {
                entity.getPreInput().executeExcept("dodge")
            }
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