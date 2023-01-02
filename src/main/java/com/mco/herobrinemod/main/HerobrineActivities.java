package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class HerobrineActivities {
    private static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Registries.ACTIVITY, HerobrineMod.MODID);

    public static void register(IEventBus eventBus) {
        ACTIVITIES.register(eventBus);
    }
}
