package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.capabilities.AnimationInfoProvider;
import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.network.AnimationInfoDataSyncPacket;
import com.mco.herobrinemod.network.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
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
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.mco.herobrinemod.entities.herobrine.base.Animations.genericWalkRunController;

public class Herobrine extends Monster implements GeoEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	// The time in the swing animation where damage should be dealt
	public final int damageTime = 10;
	private final int explosionPower = 1;
	private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

	public enum State {
		IDLE, WALK, SPRINT, SWING, BLAZE, GHAST, SUMMON
	}

	public State currentState = State.IDLE;

	public Map<String, AnimationController.State> states;
	public Map<String, Integer> timers;
	public Map<String, Integer> cooldowns;
	private ArrayList<String> uninterruptible;
	private ArrayList<Goal> attackGoals;
	private final Goal melee = new DelayedMeleeAttackGoal(this, 1.75D, true);
	private final Goal ghast = new ShootFireballGoal(this);
	public Pair<String, Integer> lastAnimation = new MutablePair<>("null", 0);

	public final Random rand = new Random();
	public final int NUM_ATTACKS = 2;
	public Herobrine(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new DelayedMeleeAttackGoal(this, 1.75D, true));
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
	}

	@Override
	public void aiStep() {
//		var bb = this.getBoundingBox();
//		this.level.addParticle(ParticleTypes.SMOKE, bb.minX, bb.maxY, bb.minZ, 0.0D, 0.0D, 0.0D);
//		this.level.addParticle(ParticleTypes.BUBBLE, bb.minX, bb.maxY, bb.maxZ, 0.0D, 0.0D, 0.0D);
//
//		this.level.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, bb.maxX, bb.maxY, bb.minZ, 0.0D, 0.0D, 0.0D);
//		this.level.addParticle(ParticleTypes.END_ROD, bb.maxX, bb.maxY, bb.maxZ, 0.0D, 0.0D, 0.0D);

		var manager = this.getAnimatableInstanceCache().getManagerForId((this).getId()).getAnimationControllers();
		for (var controller : states.keySet()) {
			var currentState = states.get(controller);
			var newState = manager.get(controller).getAnimationState();

			if (!level.isClientSide()) {
				cooldowns.put(controller, Math.max(cooldowns.get(controller) - 1, 0));
				if (currentState.equals(AnimationController.State.STOPPED)) {
					// We are starting an animation, start the timer
					if (newState.equals(AnimationController.State.RUNNING)) {
						this.lastAnimation = new MutablePair<>(controller, 1);
						System.out.println("Last anim " + lastAnimation);
					}
				}
				boolean idle = true;
				for(var state : states.keySet()) {
					if(!states.get(state).equals(AnimationController.State.STOPPED)) {
						idle = false;
					}
				}
				if(idle) {
					attackGoals.get(rand.nextInt(attackGoals.size())).start();
				}
				System.out.println(cooldowns);
				System.out.println(states);
				System.out.println(timers.entrySet());
			}
			else {
				if (currentState.equals(AnimationController.State.STOPPED)) {
					// We are starting an animation, start the timer
					if (newState.equals(AnimationController.State.RUNNING)) {
						timers.put(controller, timers.get(controller) + 1);
						System.out.println("Starting state " + controller);
					} else {
						timers.put(controller, 0);
					}
				}

				if (currentState.equals(AnimationController.State.RUNNING)) {
					String name;
					// We are stopping an animation, reset the timer
					if (newState.equals(AnimationController.State.STOPPED)) {
						System.out.println("Stopping state " + controller + " at time " + timers.get(controller));
						timers.put(controller, 0);
						name = "null";
					} else {
						timers.put(controller, timers.get(controller) + 1);
						name = controller;

//							 This animation should not be interrupted: Stop all other active animations if not on cooldown
						if (uninterruptible.contains(controller) && cooldowns.get(controller) == 0) {
							for (var c : states.keySet()) {
								if (c.equals(controller)) continue;
								// If an attack is already running, cancel this instead
								if(manager.get(c).getAnimationState().equals(AnimationController.State.RUNNING) && c.equals("Walk/Run")) {
									manager.get(controller).stop();
									timers.put(controller, 0);
								}
								System.out.println("Interrupting " + c);
								manager.get(c).stop();
								timers.put(c, 0);
							}
						}
					}
					getCapability(AnimationInfoProvider.ANIMATION_INFO).ifPresent(animationInfo -> {
						PacketHandler.sendToServer(new AnimationInfoDataSyncPacket(timers.get(controller), name));
					});
				}

				states.put(controller, newState);
			}
		}
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

		this.attackAnim = (float) this.swingTime / (float) i;
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
	public void onAddedToWorld() {
		super.onAddedToWorld();
		states = new HashMap<>();
		timers = new HashMap<>();
		cooldowns = new HashMap<>();
		uninterruptible = new ArrayList<>();
		attackGoals = new ArrayList<>();

		uninterruptible.add("Ghast");
		uninterruptible.add("Blaze");
		uninterruptible.add("Summon");

		attackGoals.add(melee);
		attackGoals.add(ghast);

		for(var controller: this.getAnimatableInstanceCache().getManagerForId((this).getId())
				.getAnimationControllers().keySet()) {
			states.put(controller, this.getAnimatableInstanceCache().getManagerForId((this).getId())
					.getAnimationControllers().get(controller).getAnimationState());
			timers.put(controller, 0);
			cooldowns.put(controller, 0);
		}
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

		public ShootFireballGoal(Herobrine herobrine) {
			this.herobrine = herobrine;
		}

		public boolean canUse() {
			return this.herobrine.getTarget() != null;
		}
		public boolean canContinueToUse() {
			return "Ghast".equals(ClientAnimationInfoData.getAnimation()) && this.herobrine.cooldowns.get("Ghast") == 0;
		}

		public void start() {
			if(this.herobrine.cooldowns.get("Ghast") == 0) {
				System.out.println(this.herobrine.cooldowns.get("Ghast"));
				this.herobrine.triggerAnim("Ghast", "herobrine_shoot");
				// If the last animation was this, increment the counter, otherwise set it to one
				int counter = 1;
				if (this.herobrine.lastAnimation.getLeft().equals("Ghast")) {
					counter = this.herobrine.lastAnimation.getRight() + 1;
				}
				this.herobrine.lastAnimation = new MutablePair<>("Ghast", counter);
			}
		}

		public void stop() {
			if(this.herobrine.cooldowns.get("Ghast") == 0) {
				System.out.println("Stopping lol xd lmao");
				this.herobrine.cooldowns.put("Ghast", (int) (60 * Math.pow(1, this.herobrine.lastAnimation.getRight() - 1)));
			}
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingentity = this.herobrine.getTarget();
			if (livingentity != null) {
				if (livingentity.distanceToSqr(this.herobrine) < 4096.0D && this.herobrine.hasLineOfSight(livingentity)) {
					Level level = this.herobrine.level;
					if (ClientAnimationInfoData.getAnimationTicks() == 10 && !this.herobrine.isSilent()) {
						level.levelEvent(null, 1015, this.herobrine.blockPosition(), 0);
					}

					herobrine.setDeltaMovement(Vec3.ZERO);
					String name = ClientAnimationInfoData.getAnimation() == null ? "null" : ClientAnimationInfoData.getAnimation();
					if ("Ghast".equals(name) && ClientAnimationInfoData.getAnimationTicks() > 5 && ClientAnimationInfoData.getAnimationTicks() < 60 && ClientAnimationInfoData.getAnimationTicks() % 5 == 0) {
						var bb = herobrine.getBoundingBox();

						double xPos;
						double zPos;
						double yPos = (bb.maxY + bb.minY) / 2.0;

						// Spawn fireball at left arm
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
					if(ClientAnimationInfoData.getAnimationTicks() >= 60) {
						this.herobrine.getCapability(AnimationInfoProvider.ANIMATION_INFO).ifPresent(animationInfo -> {
							PacketHandler.sendToServer(new AnimationInfoDataSyncPacket(0, "Ghast"));
						});
//						this.stop();
					}
				}
			}
		}
	}
}
