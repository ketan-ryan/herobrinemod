package com.mco.herobrinemod.capabilities;

import net.minecraft.nbt.CompoundTag;

public class AnimationInfo implements IAnimationInfo {
    private int animationTicks;
    private String animation;
    @Override
    public int getAnimationTicks() {
        return animationTicks;
    }

    @Override
    public String getAnimation() {
        return animation;
    }

    @Override
    public void setAnimationTicks(int animationTicks) {
        this.animationTicks = animationTicks;
    }

    @Override
    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("animationTicks", animationTicks);
        nbt.putString("animation", animation == null ? "" : animation);
    }

    public void loadNBTData(CompoundTag nbt) {
        setAnimationTicks(nbt.getInt("animationTicks"));
        setAnimation(nbt.getString("animation"));
    }
}
