package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.FireballShoot;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.HerobrineAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static software.bernie.geckolib.constant.DefaultAnimations.genericWalkRunIdleController;

public class Herobrine extends Monster implements GeoEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

	public Herobrine(EntityType<? extends Monster> type, Level level) {
		super(type, level);
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

	protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
		return HerobrineAi.makeBrain(this, dynamic);
	}

	public @NotNull Brain<Herobrine> getBrain() {
		return (Brain<Herobrine>) super.getBrain();
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source.isFire();
	}

	private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack.strike");
	private static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenPlay("attack.cast");
	private static final RawAnimation DEATH_ANIM = RawAnimation.begin().thenPlayAndHold("misc.death");


	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(
				genericWalkRunIdleController(this),
				new AnimationController<>(this, "Strike", 5, state -> PlayState.STOP)
						.triggerableAnim("herobrine_strike", ATTACK_ANIM),
				new AnimationController<>(this, "Fireball", 5, state -> PlayState.STOP)
						.triggerableAnim("herobrine_shoot", SHOOT_ANIM),
				new AnimationController<>(this, "Death", 5, state -> PlayState.STOP)
						.triggerableAnim("herobrine_death", DEATH_ANIM)
		);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@org.jetbrains.annotations.Nullable private Player unlimitedLastHurtByPlayer = null;
	public void aiStep() {
		super.aiStep();
		// lastHurtByPlayer is cleared after 100 ticks, capture it indefinitely in unlimitedLastHurtByPlayer for LivingExperienceDropEvent
		if (this.lastHurtByPlayer != null) this.unlimitedLastHurtByPlayer = lastHurtByPlayer;
		if (this.unlimitedLastHurtByPlayer != null && this.unlimitedLastHurtByPlayer.isRemoved()) this.unlimitedLastHurtByPlayer = null;
	}

	@Override
	protected void customServerAiStep() {
		ServerLevel serverlevel = (ServerLevel)this.level;
		serverlevel.getProfiler().push("herobrineBrain");
		this.getBrain().tick(serverlevel, this);
		this.level.getProfiler().pop();
		super.customServerAiStep();

		HerobrineAi.updateActivity(this);
		if(ClientAnimationInfoData.getAnimation() != null) {
			if(ClientAnimationInfoData.getAnimationTicks() > 0)
				ClientAnimationInfoData.setAnimationTicks(ClientAnimationInfoData.getAnimationTicks() - 1);
			else {
				ClientAnimationInfoData.setAnimationTicks(0);
				ClientAnimationInfoData.setAnimation("finished");
			}
		}
		if(brain.isActive(Activity.IDLE)) {
			setSprinting(false);
		} else if(brain.isActive(Activity.FIGHT)) {
			setSprinting((getDeltaMovement().length() - Math.abs(getDeltaMovement().y)) > 0.0);
			if(ClientAnimationInfoData.getAnimation() != null && ClientAnimationInfoData.getAnimation().equals("fireball")) {
				setDeltaMovement(0, 0, 0);
				setSprinting(false);
			}
		}
		this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected void tickDeath() {
		if (this.deathTime == 0) {
			triggerAnim("Death", "herobrine_death");
		}
		++this.deathTime;
		if(this.deathTime >= 160) {
			LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level);
			if (lightningbolt != null) {
				lightningbolt.moveTo(Vec3.atBottomCenterOf(this.blockPosition()));
				lightningbolt.setCause(null);
				this.level.addFreshEntity(lightningbolt);
			}
		}
		if (this.deathTime >= 200 && !this.level.isClientSide() && !this.isRemoved()) {
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

	@Contract("null->false")
	public boolean canTargetEntity(@Nullable Entity entity) {
		if (entity instanceof LivingEntity livingentity) {
			return this.level == entity.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
					&& !this.isAlliedTo(entity) && livingentity.getType() != EntityType.ARMOR_STAND
					&& !(livingentity instanceof Herobrine) && !livingentity.isInvulnerable()
					&& !livingentity.isDeadOrDying()
					&& this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
		}

		return false;
	}
	public boolean hurt(@NotNull DamageSource source, float amount) {
		boolean flag = super.hurt(source, amount);
		if (this.level.isClientSide) {
			return false;
		} else if (flag && source.getEntity() instanceof LivingEntity livingentity && !(source.getEntity() instanceof Herobrine)) {
			if (this.canAttack(livingentity) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(this, livingentity, 4.0D)) {
				if(getTarget() == null)
					this.setAttackTarget(livingentity);
				else if(!getTarget().equals(livingentity))
					this.setAttackTarget(livingentity);
			}
			return true;
		} else {
			return flag;
		}
	}

	public void setAttackTarget(LivingEntity entity) {
		setTarget(entity);
		this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
		this.brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(entity, true));
		this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, entity);
		FireballShoot.setCooldown(this, 120);
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	/**
	 * Don't despawn boss mob
	 */
	public void checkDespawn() {
		if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
			this.discard();
		} else {
			this.noActionTime = 0;
		}
	}

	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (this.hasCustomName()) {
			this.bossEvent.setName(this.getDisplayName());
		}
	}

	public void setCustomName(@Nullable Component component) {
		super.setCustomName(component);
		this.bossEvent.setName(this.getDisplayName());
	}

	public void startSeenByPlayer(@NotNull ServerPlayer serverPlayer) {
		super.startSeenByPlayer(serverPlayer);
		this.bossEvent.addPlayer(serverPlayer);
	}

	public void stopSeenByPlayer(@NotNull ServerPlayer serverPlayer) {
		super.stopSeenByPlayer(serverPlayer);
		this.bossEvent.removePlayer(serverPlayer);
	}
}
