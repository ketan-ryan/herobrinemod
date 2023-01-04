package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.FireballShoot;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.HerobrineAi;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.HerobrineMeleeAttack;
import com.mco.herobrinemod.entities.herobrine.phase2.HardHerobrine;
import com.mco.herobrinemod.main.HerobrineUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.ArrayList;

import static com.mco.herobrinemod.main.HerobrineUtils.SPEED_MULTIPLIER_WHEN_FIGHTING;

public class HardHerobrineAi extends HerobrineAi {
    public static Brain<?> makeBrain(HardHerobrine herobrine, Dynamic<?> dynamic) {
        ArrayList<MemoryModuleType<?>> MEM = new ArrayList<>(HerobrineUtils.BASE_MEMORY_TYPES);
        MEM.addAll(HerobrineUtils.HARD_MEMORY_TYPES);
        Brain.Provider<HardHerobrine> provider = Brain.provider(MEM, HerobrineUtils.HEROBRINE_SENSOR_TYPES);
        Brain<HardHerobrine> brain = provider.makeBrain(dynamic);
        initCoreActivity(brain);
        initFightActivity(herobrine, brain);
        initIdleActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    protected static void initFightActivity(Herobrine herobrine, Brain<? extends Herobrine> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.create(),
                        SetEntityLookTarget.create(
                                (entity) -> isTarget(herobrine, entity), (float) herobrine.getAttributeValue(Attributes.FOLLOW_RANGE)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.25F),
                        new HerobrineMeleeAttack(),
                        new FireballShoot(),
                        new BreatheFire()),
                MemoryModuleType.ATTACK_TARGET);
    }

}
