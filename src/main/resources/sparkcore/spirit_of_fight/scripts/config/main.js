SOFConfig.apply({
    // 实体默认韧性
    // 韧性：由本模组产生的攻击基本都会有固定的削韧值，当实体韧性降为0时，会触发受击动画（硬直），因此在当前版本中如果该实体尚未实装受击动画则无效
    // 设为25则能保证大部分攻击都可以触发受击动画，因为目前大部分攻击的削韧都不会低于25
    // 修改后若不重启世界只有新生成的此类实体会应用新的韧性，重启世界后则所有实体都会应用新的韧性
    EntityDefaultPoiseValue: {
        "minecraft:player": 100,
        "minecraft:zombie": 25,
        "minecraft:husk": 25,
        "minecraft:vindicator": 25
    }
})