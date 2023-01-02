package com.mco.herobrinemod.client;

public class ClientAnimationInfoData {
    private static int animationTicks;
    private static String animation;

    public static void setAnimationTicks(int animationTicks) {
        ClientAnimationInfoData.animationTicks = animationTicks;
    }

    public static int getAnimationTicks() {
        return animationTicks;
    }

    public static void setAnimation(String animation) {
        ClientAnimationInfoData.animation = animation;
    }

    public static String getAnimation() {
        return animation;
    }
}
