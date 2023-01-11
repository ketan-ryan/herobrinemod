package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.entities.herobrine.AbstractHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.FireballShoot;
import com.mco.herobrinemod.entities.herobrine.phase2.HardHerobrine;
import com.mco.herobrinemod.main.HerobrineEntities;
import com.mco.herobrinemod.util.HerobrineUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;

public class Herobrine extends AbstractHerobrine {
	private boolean isClone = false;

	public Herobrine(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	public void setClone() {
		this.isClone = true;
	}

	public final int EXPLOSION_POWER = 1;

	public int getExplosionPower() {
		return EXPLOSION_POWER;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 666.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.29F)
				.add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.ARMOR, 6.0D)
				.add(Attributes.FOLLOW_RANGE, 66.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		commonAiCode();
	}

	@Override
	public BrainActivityGroup<AbstractHerobrine> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new InvalidateAttackTarget<>(),
				new SetWalkTargetToAttackTarget<>()
						.speedMod(HerobrineUtils.SPEED_MULTIPLIER_WHEN_FIGHTING)
						.startCondition(entity -> getState().equals("finished")),
				new AnimatableMeleeAttack<>(7)
						.attackInterval(entity -> 14)
						.whenStarting(entity -> {
							triggerAnim("Attack", "herobrine_strike");
							setState("swing");
							setAnimationTicks(14);
						}),
				new FireballShoot()
		);
	}

	@Override
	protected AnimationController<AbstractHerobrine> getAttackController() {
		return new AnimationController<AbstractHerobrine>(this, "Attack", 5, state -> {
			if(getState().equals("fireball"))
				return state.setAndContinue(SHOOT_ANIM);
			if(getState().equals("swing"))
				return state.setAndContinue(ATTACK_ANIM);
			return PlayState.STOP;
		}).triggerableAnim("herobrine_strike", ATTACK_ANIM).triggerableAnim("herobrine_shoot", SHOOT_ANIM);
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if(isClone) {
			this.remove(Entity.RemovalReason.KILLED);
		}
		if (this.deathTime == 0) {
			triggerAnim("Death", "herobrine_death");
		}
		++this.deathTime;
		if(this.deathTime >= 160 && this.deathTime < 200) {
			LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level);
			if (lightningbolt != null) {
				lightningbolt.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
				lightningbolt.setCause(null);
				this.level.addFreshEntity(lightningbolt);
			}
		}
		if (this.deathTime >= 200 && !this.level.isClientSide() && !this.isRemoved()) {
			if(!isClone) {
				HardHerobrine phase = new HardHerobrine(HerobrineEntities.HARD_HEROBRINE.get(), this.getLevel());
				phase.setPos(this.position());
				this.getLevel().addFreshEntity(phase);
			}
			this.remove(Entity.RemovalReason.KILLED);
		}

		boolean flag = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
		if (this.level instanceof ServerLevel) {
			if (this.deathTime > 15 && this.deathTime % 5 == 0 && flag) {
				int award = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.unlimitedLastHurtByPlayer, Mth.floor((float) 6000 * 0.08F));
				ExperienceOrb.award((ServerLevel) this.level, this.position(), award);
			}
		}
	}
}
