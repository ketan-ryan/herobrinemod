package com.mco.herobrinemod.entities.herobrine.base.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mco.herobrinemod.entities.herobrine.base.BaseHerobrine;
import com.mco.herobrinemod.main.HerobrineMemoryModules;
import com.mco.herobrinemod.main.HerobrineSensors;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

public class BaseHerobrineAi {
    private static final float SPEED_MULTIPLIER_WHEN_FIGHTING = 1.75F;
    private static final List<SensorType<? extends Sensor<? super BaseHerobrine>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_PLAYERS, SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY,
            HerobrineSensors.HEROBRINE_ENTITY_SENSOR.get());

    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(
            MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.RECENT_PROJECTILE, HerobrineMemoryModules.ATTACK_DELAY.get(),
            HerobrineMemoryModules.FIREBALL_SHOOT_COOLDOWN.get(), HerobrineMemoryModules.FIREBALL_SHOOT_INTERVAL.get(),
            HerobrineMemoryModules.FIREBALL_SHOOT_DELAY.get()
    );

    public static void updateActivity(BaseHerobrine herobrine) {
        herobrine.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static Brain<?> makeBrain(BaseHerobrine herobrine, Dynamic<?> dynamic) {
        Brain.Provider<BaseHerobrine> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BaseHerobrine> brain = provider.makeBrain(dynamic);
        initCoreActivity(brain);
        initFightActivity(herobrine, brain);
        initIdleActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<BaseHerobrine> herobrine) {
        herobrine.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }

    private static void initIdleActivity(Brain<BaseHerobrine> herobrine) {
        herobrine.addActivity(Activity.IDLE, 10, ImmutableList.of(
                                                //          Distance                    min, max for ticker to start
                SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)),
                new RunOne<>(ImmutableList.of(
                                                // Speed modifier
                        Pair.of(RandomStroll.stroll(0.7F), 2),
                                                //           Speed modifier, CloseEnoughDist to stop walking
                        Pair.of(SetWalkTargetFromLookTarget.create(0.7F, 3), 2),
                        //                          Min duration, max duration
                        Pair.of(new DoNothing(30, 60), 1)))));
    }

    private static void initFightActivity(BaseHerobrine herobrine, Brain<BaseHerobrine> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.create(),
                        SetEntityLookTarget.create(
                                (entity) -> isTarget(herobrine, entity), (float) herobrine.getAttributeValue(Attributes.FOLLOW_RANGE)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(SPEED_MULTIPLIER_WHEN_FIGHTING), new HerobrineMeleeAttack(), new FireballShoot()),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean isTarget(BaseHerobrine herobrine, LivingEntity entity) {
        return herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter((targets) -> targets == entity).isPresent();
    }

}
