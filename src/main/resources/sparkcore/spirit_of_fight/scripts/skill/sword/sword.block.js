Skill.createBy("spirit_of_fight:sword.block", "spirit_of_fight:block", builder => {
    builder.addEntityAnimatableCondition()
    builder.acceptConfig((config, skill) => {
        config.set("anim_path", "minecraft:player/sword.block")
        config.set("hurt_movement", [0.0, 0.0, 1.0])
        config.set("block_body", PhysicsHelper.createCollisionBoxBoundToBone(skill.getHolder(), 'rightItem', [1.0, 1.0, 2.0], [0.0, 0.0, -0.75], body => { body.setCollideWithGroups(1) }))
    })
    builder.accept(skill => {
        const entity = skill.getHolder()
        const level = skill.getLevel()
        const config = skill.getConfig()

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