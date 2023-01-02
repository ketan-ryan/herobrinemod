package com.mco.herobrinemod.entities.herobrine.base.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.mco.herobrinemod.entities.herobrine.base.BaseHerobrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class HerobrineEntitySensor extends NearestLivingEntitySensor<BaseHerobrine> {
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ATTACK_TARGET)));
    }

    protected void doTick(@NotNull ServerLevel level, @NotNull BaseHerobrine herobrine) {
        super.doTick(level, herobrine);
        getClosest(herobrine, (closestPlayer) -> closestPlayer.getType() == EntityType.PLAYER).or(() -> {
            return getClosest(herobrine, (closestEntity) -> {
                return closestEntity.getType() != EntityType.PLAYER;
            });
        }).ifPresentOrElse((entity) -> {
            herobrine.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, entity);
        }, () -> {
            herobrine.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        });
    }

    private static Optional<LivingEntity> getClosest(BaseHerobrine herobrine, Predicate<LivingEntity> predicate) {
        return herobrine.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream).filter(herobrine::canTargetEntity).filter(predicate).findFirst();
    }

    protected int radiusXZ() {
        return 24;
    }

    protected int radiusY() {
        return 24;
    }
}