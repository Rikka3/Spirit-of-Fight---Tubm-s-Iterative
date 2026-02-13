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
    },
    // 持有特定Tag物品的状态动画组
    // 键为持有物品的tag，值为状态动画的前缀名，比如"sword"下，则会自动检测动画库里是否存在名为"state.sword.land.idle"这样格式的动画，一旦找到这种命名的动画即会在指定状态下播放
    HeldItemStateAnimation: {
        "minecraft:swords": "sword",
        "c:hammers": "hammer",
        "c:gloves": "gloves",
        "c:spears": "spear",
        "minecraft:axes": "axe",
        "c:axes": "axe"
    }
})