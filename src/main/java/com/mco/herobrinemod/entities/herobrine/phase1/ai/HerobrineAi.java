package com.mco.herobrinemod.entities.herobrine.phase1.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import com.mco.herobrinemod.main.HerobrineUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class HerobrineAi {
    public static void updateActivity(Herobrine herobrine) {
        herobrine.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static Brain<?> makeBrain(Herobrine herobrine, Dynamic<?> dynamic) {
        Brain.Provider<Herobrine> provider = Brain.provider(HerobrineUtils.BASE_MEMORY_TYPES, HerobrineUtils.HEROBRINE_SENSOR_TYPES);
        Brain<Herobrine> brain = provider.makeBrain(dynamic);
        initCoreActivity(brain);
        initFightActivity(herobrine, brain);
        initIdleActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    protected static void initCoreActivity(Brain<? extends Herobrine> herobrine) {
        herobrine.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }

    protected static void initIdleActivity(Brain<? extends Herobrine> herobrine) {
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

    protected static void initFightActivity(Herobrine herobrine, Brain<? extends Herobrine> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.create(),
                        SetEntityLookTarget.create(
                                (entity) -> isTarget(herobrine, entity), (float) herobrine.getAttributeValue(Attributes.FOLLOW_RANGE)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(HerobrineUtils.SPEED_MULTIPLIER_WHEN_FIGHTING), new HerobrineMeleeAttack(), new FireballShoot()),
                MemoryModuleType.ATTACK_TARGET);
    }

    protected static boolean isTarget(Herobrine herobrine, LivingEntity entity) {
        return herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter((targets) -> targets == entity).isPresent();
    }

}
