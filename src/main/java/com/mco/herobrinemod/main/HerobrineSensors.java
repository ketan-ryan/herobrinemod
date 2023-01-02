package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.entities.herobrine.base.ai.HerobrineEntitySensor;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static net.minecraft.util.Unit.INSTANCE;

public class HerobrineSensors {
    private static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(Registries.SENSOR_TYPE, HerobrineMod.MODID);

    public static final RegistryObject<SensorType<HerobrineEntitySensor>> HEROBRINE_ENTITY_SENSOR =  SENSORS.register("herobrine_entity_sensor",
            () -> new SensorType<>(HerobrineEntitySensor::new));

    public static void register(IEventBus eventBus) {
        SENSORS.register(eventBus);
    }
}
