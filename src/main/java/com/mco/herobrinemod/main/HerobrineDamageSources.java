package com.mco.herobrinemod.main;

import net.minecraft.util.DamageSource;

public class HerobrineDamageSources extends DamageSource {

    public HerobrineDamageSources(String name){
        super(name);
    }

    public static DamageSource HARD_HEROBRINE = new HerobrineDamageSources("hard_herobrine");
    public static DamageSource HARD_LASER = new HerobrineDamageSources("hard_laser");
    public static DamageSource HARD_SWORD = new HerobrineDamageSources("hard_sword");
}
