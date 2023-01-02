package com.mco.herobrinemod.entities.herobrine.base.ai;

import com.google.common.collect.ImmutableMap;
import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.entities.herobrine.base.BaseHerobrine;
import com.mco.herobrinemod.main.HerobrineMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

public class HerobrineMeleeAttack extends Behavior<BaseHerobrine> {
    private static final int DURATION = Mth.ceil(14.0F);
    private static final int ATTACK_DELAY = Mth.ceil(7.0D);

    public HerobrineMeleeAttack() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                HerobrineMemoryModules.ATTACK_DELAY.get(), MemoryStatus.REGISTERED
        ), DURATION);
    }

    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, BaseHerobrine herobrine) {
        // length       height
        return herobrine.isWithinMeleeAttackRange(herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get());
    }

    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull BaseHerobrine herobrine, long l) {
        return true;
    }

    protected void start(@NotNull ServerLevel level, BaseHerobrine herobrine, long l) {
        herobrine.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, DURATION);
        herobrine.getBrain().setMemoryWithExpiry(HerobrineMemoryModules.ATTACK_DELAY.get(), Unit.INSTANCE, ATTACK_DELAY);
        herobrine.triggerAnim("Strike", "herobrine_strike");
        if(ClientAnimationInfoData.getAnimationTicks() == 0) {
            ClientAnimationInfoData.setAnimation("swing");
            ClientAnimationInfoData.setAnimationTicks(14);
        }
    }

    protected void tick(@NotNull ServerLevel level, BaseHerobrine herobrine, long l) {
        herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> herobrine.getLookControl().setLookAt(target.position()));
        if (!herobrine.getBrain().hasMemoryValue(HerobrineMemoryModules.ATTACK_DELAY.get())) {
            herobrine.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(herobrine::canTargetEntity).ifPresent((target) -> {
                if(herobrine.isWithinMeleeAttackRange(target)) {
                    herobrine.swing(InteractionHand.MAIN_HAND);
                    herobrine.doHurtTarget(target);
                }
            });
        }
    }

    protected void stop(@NotNull ServerLevel level, @NotNull BaseHerobrine herobrine, long l) {
    }
}
