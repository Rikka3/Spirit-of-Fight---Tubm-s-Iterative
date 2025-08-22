Skill.createBy("spirit_of_fight:sword.block", "spirit_of_fight:block", builder => {
    builder.acceptConfig(config => {
        config.set("anim_path", "minecraft:player/sword.block")
        config.set("hurt_movement", [0.0, 0.0, 1.0])
    })
    builder.accept(skill => {
        const entity = skill.getHolderWrapper().asEntity()
        const animatable = skill.getHolderWrapper().asAnimatable()
        const level = skill.getLevel()
        const config = skill.getConfig()
        config.set("block_body", PhysicsHelper.createCollisionBoxBoundToBone(animatable, 'rightItem', [1.0, 1.0, 2.0], [0.0, 0.0, -0.75], body => { body.setCollideWithGroups(1) }))


        if (entity == null || animatable == null) return

        skill.onBlockHurt((event, hitPos) => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_block", "players")
            SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:crit')
        })

        skill.onPrecisionBlock((event, hitPos) => {
            level.playSound(entity.getOnPos().above(), "spirit_of_fight:sharp_parry_1", "players")
            SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:firework')
            SOFParticlePresets.summonQuadraticParticle(level, hitPos, 15, 'minecraft:flame')
        })
    })
})