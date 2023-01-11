package com.mco.herobrinemod.entities.herobrine;

import com.mco.herobrinemod.entities.herobrine.phase1.ai.HerobrineSensor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.WalkOrRunToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
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
import java.util.List;

import static software.bernie.geckolib.constant.DefaultAnimations.genericWalkRunIdleController;

public abstract class AbstractHerobrine extends Monster implements GeoEntity, SmartBrainOwner<AbstractHerobrine> {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    public static final EntityDataAccessor<String> STATE = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> ANIMATION_TICKS = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.INT);
    protected AbstractHerobrine(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack.strike");
    protected static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenPlay("attack.cast");
    protected static final RawAnimation DEATH_ANIM = RawAnimation.begin().thenPlayAndHold("misc.death");

    protected abstract AnimationController<AbstractHerobrine> getAttackController();

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                genericWalkRunIdleController(this),
                getAttackController(),
                new AnimationController<>(this, "Death", 3, state -> getState().equals("dead") ? state.setAndContinue(DEATH_ANIM) : PlayState.STOP)
                        .triggerableAnim("herobrine_death", DEATH_ANIM)
        );
    }

    @Override
    protected Brain.@NotNull Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<ExtendedSensor<AbstractHerobrine>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<AbstractHerobrine>()
                        .setRadius(66),
                new HurtBySensor<>(),
                new HerobrineSensor<AbstractHerobrine>()
                        .setRadius(66)
                        .setPredicate((target, entity) -> canTargetEntity(target)),
                new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<AbstractHerobrine> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new WalkOrRunToWalkTarget<>()
                        .startCondition(entity -> getState().equals("finished"))
        );
    }

    @Override
    public BrainActivityGroup<AbstractHerobrine> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<AbstractHerobrine>(
                        new TargetOrRetaliate<>()
                                .attackablePredicate(this::canTargetEntity),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new SetWalkTargetToAttackTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    public abstract int getExplosionPower();

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isFire() || source.isFall();
    }

    @org.jetbrains.annotations.Nullable
    protected Player unlimitedLastHurtByPlayer = null;
    public void aiStep() {
        super.aiStep();
        // lastHurtByPlayer is cleared after 100 ticks, capture it indefinitely in unlimitedLastHurtByPlayer for LivingExperienceDropEvent
        if (this.lastHurtByPlayer != null) this.unlimitedLastHurtByPlayer = lastHurtByPlayer;
        if (this.unlimitedLastHurtByPlayer != null && this.unlimitedLastHurtByPlayer.isRemoved()) this.unlimitedLastHurtByPlayer = null;
    }

    protected void commonAiCode() {
        tickBrain(this);
        if(this.getState() != null) {
            if(this.getAnimationTicks() > 0)
                this.setAnimationTicks(this.getAnimationTicks() - 1);
            else {
                this.setAnimationTicks(0);
                this.setState("finished");
            }
        }
        if(brain.isActive(Activity.IDLE)) {
            setSprinting(false);
        } else if(brain.isActive(Activity.FIGHT)) {
            setSprinting((getDeltaMovement().length() - Math.abs(getDeltaMovement().y)) > 0.0);
            if(this.getState() != null &&
                    (this.getState().equals("fireball") || this.getState().equals("breathe"))) {
                setDeltaMovement(0, getDeltaMovement().y, 0);
                setSprinting(false);
            }
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Contract("null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingentity) {
            return this.level == entity.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
                    && !this.isAlliedTo(entity) && livingentity.getType() != EntityType.ARMOR_STAND
                    && !(livingentity instanceof AbstractHerobrine) && !livingentity.isInvulnerable()
                    && !livingentity.isDeadOrDying()
                    && this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
        }

        return false;
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

    @Override
    protected void tickDeath() {
        setState("dead");
        this.bossEvent.setProgress(0.0F);
    }

    public String getState() {
        return this.entityData.get(STATE);
    }

    public void setState(String state) {
        this.entityData.set(STATE, state);
    }

    public Integer getAnimationTicks() {
        return this.entityData.get(ANIMATION_TICKS);
    }

    public void setAnimationTicks(Integer animationTicks) {
        this.entityData.set(ANIMATION_TICKS, animationTicks);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, "finished");
        this.entityData.define(ANIMATION_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("State", this.getState());
        tag.putInt("AnimationTicks", this.getAnimationTicks());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
        this.setState(tag.getString("State"));
        this.setAnimationTicks(tag.getInt("AnimationTicks"));
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
