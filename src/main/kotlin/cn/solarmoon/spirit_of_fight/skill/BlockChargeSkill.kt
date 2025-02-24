package cn.solarmoon.spirit_of_fight.skill

//class BlockChargeSkill(
//    blockSkill: SuperArmorBlockSkill,
//    attackSkill: CommonAttackSkill,
//    val chargingParticle: ParticleOptions = ParticleTypes.WHITE_SMOKE
//): CompositeSkill(listOf(blockSkill, attackSkill)) {
//
//    val blockSkill get() = children[0] as SuperArmorBlockSkill
//    val attackSkill get() = children[1] as CommonAttackSkill
//
//    override fun onActive() {
//        val entity = holder as? Entity ?: return
//        val fs = entity.getFightSpirit()
//
//        blockSkill.activate()
//        blockSkill.blockAnim.rejectNewAnim = { it?.name != attackSkill.animation.name }
//
//        blockSkill.blockAnim.onEvent<AnimEvent.Tick> {
//
//        }
//
//        blockSkill.blockAnim.onEvent<AnimEvent.Completed> {
//            attackSkill.activate()
//        }
//
//        attackSkill.onEvent<SkillEvent.Active> {
//            if (!fs.isFull) {
//                this@BlockChargeSkill.end()
//            } else {
//                entity.getFightSpirit().clear()
//            }
//        }
//    }
//
//    override fun onUpdate() {
//        super.onUpdate()
//        if (!attackSkill.isActive && !blockSkill.isActive) end()
//    }
//
//    override val codec: MapCodec<out Skill> = CODEC
//
//    companion object {
//        val CODEC: MapCodec<BlockChargeSkill> = RecordCodecBuilder.mapCodec {
//            it.group(
//                SuperArmorBlockSkill.CODEC.fieldOf("block_skill").forGetter { it.blockSkill },
//                CommonAttackSkill.CODEC.fieldOf("attack_skill").forGetter { it.attackSkill },
//                ParticleTypes.CODEC.optionalFieldOf("charging_particle", ParticleTypes.WHITE_SMOKE).forGetter { it.chargingParticle }
//            ).apply(it, ::BlockChargeSkill)
//        }
//    }
//
//}