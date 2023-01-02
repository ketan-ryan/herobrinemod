package com.mco.herobrinemod.capabilities;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IAnimationInfo {
    int getAnimationTicks();
    String getAnimation();

    void setAnimationTicks(int animationTicks);
    void setAnimation(String animation);
}
