package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.HerobrineAi;
import com.mco.herobrinemod.entities.herobrine.phase2.ai.HardHerobrineAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class HardHerobrine extends Herobrine implements GeoEntity {
    public HardHerobrine(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public final int EXPLOSION_POWER = 1;

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.50F)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 166.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return HardHerobrineAi.makeBrain(this, dynamic);
    }

    private static final RawAnimation BREATHE_ANIM = RawAnimation.begin().thenPlay("attack.breathe");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "Breathe", 5, state -> PlayState.STOP)
                .triggerableAnim("herobrine_breathe", BREATHE_ANIM));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        System.out.println(ClientAnimationInfoData.getAnimation());
    }

    @Override
    public float getStepHeight() {
        return 5;
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource source) {
        return false;
    }
}
