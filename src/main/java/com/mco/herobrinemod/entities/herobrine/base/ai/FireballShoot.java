package com.mco.herobrinemod.entities.herobrine.base.ai;

import com.google.common.collect.ImmutableMap;
import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.entities.herobrine.base.BaseHerobrine;
import com.mco.herobrinemod.main.HerobrineMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FireballShoot extends Behavior<BaseHerobrine> {
    private static final int DURATION = Mth.ceil(60.0F);
    private static final int ATTACK_DELAY = Mth.ceil(10.0D);

    public FireballShoot() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                HerobrineMemoryModules.FIREBALL_SHOOT_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT,
                HerobrineMemoryModules.FIREBALL_SHOOT_DELAY.get(), MemoryStatus.REGISTERED,
                HerobrineMemoryModules.FIREBALL_SHOOT_INTERVAL.get(), MemoryStatus.REGISTERED
            ), DURATION);
    }

    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, BaseHerobrine herobrine) {
                                                                                                                // length       height
        return herobrine.closerThan(herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), 15.0D, 20.0D);
    }

    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BaseHerobrine herobrine, long l) {
        return true;
    }

    protected void start(@NotNull ServerLevel level, BaseHerobrine herobrine, long l) {
        herobrine.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, DURATION);
        herobrine.getBrain().setMemoryWithExpiry(HerobrineMemoryModules.FIREBALL_SHOOT_DELAY.get(), Unit.INSTANCE, ATTACK_DELAY);
        herobrine.triggerAnim("Fireball", "herobrine_shoot");
        ClientAnimationInfoData.setAnimation("fireball");
        ClientAnimationInfoData.setAnimationTicks(60);
    }

    protected void tick(@NotNull ServerLevel level, BaseHerobrine herobrine, long l) {
        herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> herobrine.getLookControl().setLookAt(target.position()));
        if (!herobrine.getBrain().hasMemoryValue(HerobrineMemoryModules.FIREBALL_SHOOT_DELAY.get()) && !herobrine.getBrain().hasMemoryValue(HerobrineMemoryModules.FIREBALL_SHOOT_INTERVAL.get())) {
            herobrine.getBrain().setMemoryWithExpiry(HerobrineMemoryModules.FIREBALL_SHOOT_INTERVAL.get(), Unit.INSTANCE, 5);
            herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(herobrine::canTargetEntity).filter((entities) -> herobrine.closerThan(entities, 15.0D, 20.0D)).ifPresent((target) -> {
                var bb = herobrine.getBoundingBox();

                double xPos;
                double zPos;
                double yPos = (bb.maxY + bb.minY) / 2.0;

                // Spawn fireball at left arm
                switch(herobrine.getDirection()) {
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

                Vec3 vec3 = herobrine.getViewVector(1.0F);
                double d2 = target.getX() - (herobrine.getX() + vec3.x);
                double d3 = target.getY() - (herobrine.getY());
                double d4 = target.getZ() - (herobrine.getZ() + vec3.z);

                if (!herobrine.isSilent()) {
                    level.levelEvent(null, 1016, herobrine.blockPosition(), 0);
                }

                LargeFireball largefireball = new LargeFireball(level, herobrine, d2, d3, d4, 1);
                largefireball.setPos(xPos, yPos, zPos);
                level.addFreshEntity(largefireball);
            });
        }
    }

    protected void stop(@NotNull ServerLevel level, @NotNull BaseHerobrine herobrine, long l) {
        setCooldown(herobrine, 50 + herobrine.getRandom().nextInt(120));
    }

    public static void setCooldown(LivingEntity entity, int duration) {
        entity.getBrain().setMemoryWithExpiry(HerobrineMemoryModules.FIREBALL_SHOOT_COOLDOWN.get(), Unit.INSTANCE, duration);
    }
}
