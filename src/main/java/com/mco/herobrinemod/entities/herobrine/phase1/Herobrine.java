package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.entities.Animations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static com.mco.herobrinemod.entities.Animations.genericWalkRunController;

public class Herobrine extends Monster implements GeoEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	// The time in the swing animation where damage should be dealt
	public final int damageTime = 10;
	private final int explosionPower = 1;
	private int animationTick = 0;
	private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

	public enum State {
		IDLE, WALK, SPRINT, SWING, BLAZE, GHAST, SUMMON
	}

	public State currentState = State.IDLE;

	public Herobrine(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	protected void registerGoals() {
//		this.goalSelector.addGoal(2, new DelayedMeleeAttackGoal(this, 1.75D, true));
		this.goalSelector.addGoal(1, new ShootFireballGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public void readAdditionalSaveData(CompoundTag p_31474_) {
		super.readAdditionalSaveData(p_31474_);
		if (this.hasCustomName()) {
			this.bossEvent.setName(this.getDisplayName());
		}

	}

	public void setCustomName(@Nullable Component p_31476_) {
		super.setCustomName(p_31476_);
		this.bossEvent.setName(this.getDisplayName());
	}

	public void startSeenByPlayer(ServerPlayer serverPlayer) {
		super.startSeenByPlayer(serverPlayer);
		this.bossEvent.addPlayer(serverPlayer);
	}

	public void stopSeenByPlayer(ServerPlayer serverPlayer) {
		super.stopSeenByPlayer(serverPlayer);
		this.bossEvent.removePlayer(serverPlayer);
	}

	protected void customServerAiStep() {
		if(this.getDeltaMovement().x == 0 && this.getDeltaMovement().z == 0) {
			this.setSprinting(false);
			if(!this.swinging && this.currentState != State.GHAST)
				currentState = State.IDLE;
		}

		else if(!isSprinting() && this.currentState != State.GHAST)
			currentState = State.WALK;

		if(isSprinting() && this.currentState != State.GHAST)
			currentState = State.SPRINT;

		if(swinging)
			currentState = State.SWING;

		this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
		System.out.println(currentState);
	}

	@Override
	public void aiStep() {
		var bb = this.getBoundingBox();
//		this.level.addParticle(ParticleTypes.SMOKE, bb.minX, bb.maxY, bb.minZ, 0.0D, 0.0D, 0.0D);
//		this.level.addParticle(ParticleTypes.BUBBLE, bb.minX, bb.maxY, bb.maxZ, 0.0D, 0.0D, 0.0D);
//
//		this.level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, bb.maxX, bb.maxY, bb.minZ, 0.0D, 0.0D, 0.0D);
//		this.level.addParticle(ParticleTypes.END_ROD, bb.maxX, bb.maxY, bb.maxZ, 0.0D, 0.0D, 0.0D);
		if(this.swinging)
			this.animationTick = this.swingTime;

		System.out.println(this.getAnimatableInstanceCache().getManagerForId((this).getId())
				.getAnimationControllers().get().getAnimationState());

//		System.out.println(this.animationTick);

		super.aiStep();
	}

	/**
	 * Override this to use a custom swing duration so the full animation plays
	 */
	@Override
	protected void updateSwingTime() {
		int i = 14;
		if (this.swinging) {
			++this.swingTime;
			if (this.swingTime >= i) {
				this.swingTime = 0;
				this.swinging = false;
			}
		} else {
			this.swingTime = 0;
		}

		this.attackAnim = (float)this.swingTime / (float)i;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 666.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.29F)
				.add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.ARMOR, 6.0D)
				.add(Attributes.FOLLOW_RANGE, 66.0D);
	}

	private static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenPlay("attack.cast");

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(
				genericWalkRunController(this),
				DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_STRIKE),
				new AnimationController<>(this, "Ghast", 5, state -> PlayState.STOP)
						.triggerableAnim("herobrine_shoot", SHOOT_ANIM)
		);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
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

	public int getExplosionPower() {
		return this.explosionPower;
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	static class ShootFireballGoal extends Goal {
		private final Herobrine herobrine;
		public int chargeTime = -99;

		private boolean started = false;

		public ShootFireballGoal(Herobrine herobrine) {
			this.herobrine = herobrine;
		}

		public boolean canUse() {
			return this.herobrine.getTarget() != null;
		}
		public boolean canContinueToUse() {
			return herobrine.animationTick < 40;
		}

		public void start() {
			if(herobrine.currentState == State.IDLE) {
				this.herobrine.currentState = State.GHAST;
				this.herobrine.triggerAnim("Ghast", "herobrine_shoot");
				System.out.println("Starting shoot");
			}
		}

		public void stop() {
			if(herobrine.currentState == State.GHAST) {
				this.herobrine.currentState = State.IDLE;
				System.out.println("Stopping shoot");
				this.herobrine.animationTick = 0;
			}
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingentity = this.herobrine.getTarget();
			if (livingentity != null) {
				this.herobrine.animationTick++;
				if (livingentity.distanceToSqr(this.herobrine) < 4096.0D && this.herobrine.hasLineOfSight(livingentity)) {
					Level level = this.herobrine.level;
					if (this.herobrine.animationTick == 10 && !this.herobrine.isSilent()) {
						level.levelEvent(null, 1015, this.herobrine.blockPosition(), 0);
					}

					if (this.herobrine.animationTick > 30 && this.herobrine.animationTick % 3 == 0) {
						var bb = herobrine.getBoundingBox();
						var mov = this.herobrine.getDeltaMovement();
						this.herobrine.setDeltaMovement(0, mov.y, 0);

						double xPos;
						double zPos;
						double yPos = (bb.maxY + bb.minY) / 2.0;
						switch(this.herobrine.getDirection()) {
							case SOUTH -> {
								xPos = bb.maxX;
								zPos = bb.maxZ;
							}
							case WEST -> {
								xPos = bb.minX;
								zPos = bb.maxZ;
							}
							case NORTH -> {
								xPos = bb.minX;
								zPos = bb.minZ;
							}
							case EAST -> {
								xPos = bb.maxX;
								zPos = bb.minZ;
							}
							default -> {
								xPos = (bb.maxX + bb.minX) / 2.0;
								zPos = (bb.maxZ + bb.minZ) / 2.0;
							}
						}

						Vec3 vec3 = this.herobrine.getViewVector(1.0F);
						double d2 = livingentity.getX() - (this.herobrine.getX() + vec3.x);
						double d3 = livingentity.getY() - (this.herobrine.getY());
						double d4 = livingentity.getZ() - (this.herobrine.getZ() + vec3.z);

						if (!this.herobrine.isSilent()) {
							level.levelEvent(null, 1016, this.herobrine.blockPosition(), 0);
						}

						LargeFireball largefireball = new LargeFireball(level, this.herobrine, d2, d3, d4, this.herobrine.getExplosionPower());
						largefireball.setPos(xPos, yPos, zPos);
						level.addFreshEntity(largefireball);
					}
					if(this.herobrine.animationTick >= 40) {
						this.stop();
					}
				}
			}
		}
	}
}
