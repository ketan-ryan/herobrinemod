package com.mco.herobrinemod.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimationInfoProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<AnimationInfo> ANIMATION_INFO = CapabilityManager.get(new CapabilityToken<>() {});

    private AnimationInfo animationInfo = null;
    private final LazyOptional<AnimationInfo> optional = LazyOptional.of(this::createAnimationInfo);

    private AnimationInfo createAnimationInfo() {
        if(this.animationInfo == null) {
            this.animationInfo = new AnimationInfo();
        }
        return this.animationInfo;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ANIMATION_INFO) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createAnimationInfo().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createAnimationInfo().loadNBTData(nbt);
    }
}
