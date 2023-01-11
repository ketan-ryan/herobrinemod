package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.entities.herobrine.AbstractHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase1.ai.HerobrineSensor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HerobrineSensors {
    private static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(Registries.SENSOR_TYPE, HerobrineMod.MODID);

    public static final RegistryObject<SensorType<HerobrineSensor<AbstractHerobrine>>> HEROBRINE_ENTITY_SENSOR =  SENSORS.register("herobrine_entity_sensor",
            () -> new SensorType<>(HerobrineSensor::new));

    public static void register(IEventBus eventBus) {
        SENSORS.register(eventBus);
    }
}
