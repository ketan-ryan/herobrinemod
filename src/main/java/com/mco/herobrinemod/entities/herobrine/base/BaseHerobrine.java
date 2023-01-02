package com.mco.herobrinemod.entities.herobrine.base;

import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.entities.herobrine.base.ai.BaseHerobrineAi;
import com.mco.herobrinemod.entities.herobrine.base.ai.FireballShoot;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
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

import static com.mco.herobrinemod.entities.herobrine.base.Animations.genericWalkRunController;

public class BaseHerobrine extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseHerobrine(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 666.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 66.0D);
    }

    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return BaseHerobrineAi.makeBrain(this, dynamic);
    }

    public @NotNull Brain<BaseHerobrine> getBrain() {
        return (Brain<BaseHerobrine>) super.getBrain();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.isFire();
    }

    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack.strike");
    private static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenPlay("attack.cast");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                genericWalkRunController(this),
                new AnimationController<>(this, "Strike", 5, state -> PlayState.STOP)
                        .triggerableAnim("herobrine_strike", ATTACK_ANIM),
                new AnimationController<>(this, "Fireball", 5, state -> PlayState.STOP)
                        .triggerableAnim("herobrine_shoot", SHOOT_ANIM)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void customServerAiStep() {
        ServerLevel serverlevel = (ServerLevel)this.level;
        serverlevel.getProfiler().push("herobrineBrain");
        this.getBrain().tick(serverlevel, this);
        this.level.getProfiler().pop();
        super.customServerAiStep();

        BaseHerobrineAi.updateActivity(this);
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
            setSprinting(true);
            if(ClientAnimationInfoData.getAnimation() != null && ClientAnimationInfoData.getAnimation().equals("fireball")) {
                setDeltaMovement(0, 0, 0);
                setSprinting(false);
                setYBodyRot(0);
                setYRot(0);
            }
            System.out.println(ClientAnimationInfoData.getAnimation());
        }
    }

    @Contract("null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingentity) {
            return this.level == entity.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
                    && !this.isAlliedTo(entity) && livingentity.getType() != EntityType.ARMOR_STAND
                    && !(livingentity instanceof BaseHerobrine) && !livingentity.isInvulnerable()
                    && !livingentity.isDeadOrDying()
                    && this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
        }

        return false;
    }
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (this.level.isClientSide) {
            return false;
        } else if (flag && source.getEntity() instanceof LivingEntity livingentity && !(source.getEntity() instanceof BaseHerobrine)) {
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
}
