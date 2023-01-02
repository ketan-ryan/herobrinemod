package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static net.minecraft.util.Unit.INSTANCE;

public class HerobrineMemoryModules {
    private static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, HerobrineMod.MODID);

    public static final RegistryObject<MemoryModuleType<Unit>> FIREBALL_SHOOT_COOLDOWN =  MEMORY_MODULES.register("fireball_shoot_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(INSTANCE))));

    public static final RegistryObject<MemoryModuleType<Unit>> FIREBALL_SHOOT_DELAY =  MEMORY_MODULES.register("fireball_shoot_delay",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(INSTANCE))));

    public static final RegistryObject<MemoryModuleType<Unit>> FIREBALL_SHOOT_INTERVAL =  MEMORY_MODULES.register("fireball_shoot_interval",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(INSTANCE))));

    public static final RegistryObject<MemoryModuleType<Unit>> ATTACK_DELAY =  MEMORY_MODULES.register("attack_delay",
            () -> new MemoryModuleType<>(Optional.of(Codec.unit(INSTANCE))));

    public static void register(IEventBus eventBus) {
        MEMORY_MODULES.register(eventBus);
    }
}
