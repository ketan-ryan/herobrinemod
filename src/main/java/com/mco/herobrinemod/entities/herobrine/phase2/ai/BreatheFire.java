package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.google.common.collect.ImmutableMap;
import com.mco.herobrinemod.entities.herobrine.AbstractHerobrine;
import com.mco.herobrinemod.main.HerobrineMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BreatheFire extends Behavior<AbstractHerobrine> {
    private static final int DURATION = Mth.ceil(90.0F);
    private static final int ATTACK_DELAY = Mth.ceil(5.0D);

    public BreatheFire() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                HerobrineMemoryModules.BREATHE_FIRE_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT,
                HerobrineMemoryModules.BREATHE_FIRE_DELAY.get(), MemoryStatus.REGISTERED
        ), DURATION);
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull AbstractHerobrine herobrine) {
        return herobrine.getState().equals("finished");
    }

    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull AbstractHerobrine herobrine, long l) {
        return true;
    }

    protected void start(@NotNull ServerLevel level, AbstractHerobrine herobrine, long l) {
        herobrine.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, DURATION);
        herobrine.getBrain().setMemoryWithExpiry(HerobrineMemoryModules.BREATHE_FIRE_DELAY.get(), Unit.INSTANCE, ATTACK_DELAY);
        herobrine.triggerAnim("Breathe", "herobrine_breathe");
        herobrine.setState("breathe");
        herobrine.setAnimationTicks(DURATION);
    }

    protected void tick(@NotNull ServerLevel level, AbstractHerobrine herobrine, long l) {
        herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> herobrine.getLookControl().setLookAt(target.position()));
        if (!herobrine.getBrain().hasMemoryValue(HerobrineMemoryModules.BREATHE_FIRE_DELAY.get())) {
            herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(herobrine::canTargetEntity).ifPresent((target) -> {
                var bb = herobrine.getBoundingBox();

                double xPos = (bb.maxX + bb.minX) / 2.0;
                double zPos = (bb.maxZ + bb.minZ) / 2.0;

                Vec3 vec3 = herobrine.getViewVector(1.0F);
                double d2 = target.getX() - (herobrine.getX() + vec3.x) + (2.0D * herobrine.getRandom().nextDouble() - 1.0D) * 2.0D;
                double d3 = target.getY() - (1.5D + herobrine.getY(0.5D) + target.getBbHeight());
                double d4 = target.getZ() - (herobrine.getZ() + vec3.z) + (2.0D * herobrine.getRandom().nextDouble() - 1.0D) * 2.0D;

                if (!herobrine.isSilent()) {
                    level.levelEvent(null, 1018, herobrine.blockPosition(), 0);
                }
                SmallFireball smallFireball = new SmallFireball(level, herobrine, d2, d3, d4);
                smallFireball.setPos(xPos, bb.minY + herobrine.getEyeHeight(), zPos);
                level.addFreshEntity(smallFireball);
            });
        }
    }

    protected void stop(@NotNull ServerLevel level, @NotNull AbstractHerobrine herobrine, long l) {
        setCooldown(herobrine, 80 + herobrine.getRandom().nextInt(120));
    }

    public static void setCooldown(LivingEntity entity, int duration) {
        entity.getBrain().setMemoryWithExpiry(HerobrineMemoryModules.BREATHE_FIRE_COOLDOWN.get(), Unit.INSTANCE, duration);
    }
}
