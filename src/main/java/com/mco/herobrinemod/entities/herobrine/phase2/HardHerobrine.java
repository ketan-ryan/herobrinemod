package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.entities.herobrine.AbstractHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.FireballShoot;
import com.mco.herobrinemod.entities.herobrine.phase2.ai.BreatheFire;
import com.mco.herobrinemod.main.HerobrineEntities;
import com.mco.herobrinemod.util.HerobrineUtils;
import com.mco.herobrinemod.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class HardHerobrine extends AbstractHerobrine {
    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(HardHerobrine.class, EntityDataSerializers.INT);
    public final int EXPLOSION_POWER = 2;

    public HardHerobrine(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 6);
    }

    public void setSize(int size) {
        this.entityData.set(ID_SIZE, size);
        this.reapplyPosition();
        this.refreshDimensions();
        this.explosionParticles();
    }

    public int getSize() {
        return this.entityData.get(ID_SIZE);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Size", this.getSize() - 1);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        this.setSize(tag.getInt("Size") + 1);
        super.readAdditionalSaveData(tag);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (ID_SIZE.equals(accessor)) {
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
        }

        super.onSyncedDataUpdated(accessor);
    }

    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        if(this.getSize() == 3F) {
            return super.getDimensions(pose).scale(0.055F * this.getSize());
        } else if(this.getSize() == 1.5F) {
            return super.getDimensions(pose).scale(0.115F * this.getSize());
        }
        return super.getDimensions(pose).scale(this.getSize());
    }


    public int getExplosionPower() {
        return EXPLOSION_POWER;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.50F)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 66.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    private static final RawAnimation BREATHE_ANIM = RawAnimation.begin().thenPlay("attack.breathe");

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
                new FireballShoot(),
                new BreatheFire()
        );
    }

    @Override
    protected AnimationController<AbstractHerobrine> getAttackController() {
        return new AnimationController<AbstractHerobrine>(this, "Attack", 5, state -> {
            if(getState().equals("fireball"))
                return state.setAndContinue(SHOOT_ANIM);
            if(getState().equals("swing"))
                return state.setAndContinue(ATTACK_ANIM);
            if(getState().equals("breathe"))
                return state.setAndContinue(BREATHE_ANIM);
            return PlayState.STOP;
        }).triggerableAnim("herobrine_strike", ATTACK_ANIM)
            .triggerableAnim("herobrine_shoot", SHOOT_ANIM)
            .triggerableAnim("herobrine_breathe", BREATHE_ANIM);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        commonAiCode();

        if (getSize() == 6) {
            if (getHealth() < getMaxHealth() / 2) {
                setSize(3);
                Herobrine herobrine = new Herobrine(HerobrineEntities.HEROBRINE.get(), this.getLevel());
                herobrine.setPos(this.position());
                herobrine.setClone();
                this.getLevel().addFreshEntity(herobrine);
            }
        } else if (getSize() == 3) {
            addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 1));
            if (getHealth() < getMaxHealth() / 4) {
                setSize(1);
            }
        } else if (getSize() == 1.5F) {
            addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 2));
            addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1, 2));
        }
    }

    @Override
    public float getStepHeight() {
        return 5;
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, @NotNull DamageSource source) {
        return false;
    }

    private void explosionParticles() {
        int radius = getSize() >= 3 ? (int) getSize() - 1 : (int) getSize();
        int height = getSize() >= 3 ? (int) getSize() * 2 : (int) getSize() * 2 + 1;
        ParticleHelper.createParticleCube(getLevel(), ParticleTypes.EXPLOSION, radius, height, 10, position(),
                getRandom().nextGaussian() * 0.005D,getRandom().nextGaussian() * 0.005D,getRandom().nextGaussian() * 0.005D);
        getLevel().playLocalSound(blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1F, 1F, true);
    }

    protected void tickDeath() {
        super.tickDeath();
        ++this.deathTime;
        if (this.deathTime >= 20 && !this.level.isClientSide() && !this.isRemoved()) {
            this.level.broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }
}
