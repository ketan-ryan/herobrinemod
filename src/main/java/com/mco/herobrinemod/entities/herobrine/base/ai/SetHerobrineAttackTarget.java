package com.mco.herobrinemod.entities.herobrine.base.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Optional;

public class SetHerobrineAttackTarget {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create((p_258331_) -> {
            return p_258331_.group(p_258331_.present(MemoryModuleType.NEAREST_ATTACKABLE), p_258331_.registered(MemoryModuleType.LOOK_TARGET), p_258331_.absent(MemoryModuleType.WALK_TARGET)).apply(p_258331_, (p_258317_, p_258318_, p_258319_) -> {
                return (p_258326_, p_258327_, p_258328_) -> {
                    if (!p_258327_.isBaby()) {
                        return false;
                    } else {
//                        AgeableMob ageablemob = p_258331_.get(p_258317_);
                        return false;
//                        if (p_258327_.closerThan(ageablemob, (double)(p_259321_.getMaxValue() + 1)) && !p_258327_.closerThan(ageablemob, (double)p_259321_.getMinValue())) {
//                            WalkTarget walktarget = new WalkTarget(new EntityTracker(ageablemob, false), p_259190_.apply(p_258327_), p_259321_.getMinValue() - 1);
//                            p_258318_.set(new EntityTracker(ageablemob, true));
//                            p_258319_.set(walktarget);
//                            return true;
//                        } else {
//                            return false;
                        }
//                    }
                };
            });
        });
    }
}
