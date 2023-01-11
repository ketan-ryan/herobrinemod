package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.HardHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HerobrineEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HerobrineMod.MODID);

	public static final RegistryObject<EntityType<Herobrine>> HEROBRINE = ENTITIES.register("herobrine", () ->
			EntityType.Builder.of(Herobrine::new, MobCategory.CREATURE)
					.sized(1F, 2F)
					.build(new ResourceLocation(HerobrineMod.MODID, "herobrine").toString()));
	public static final RegistryObject<EntityType<HardHerobrine>> HARD_HEROBRINE = ENTITIES.register("hard_herobrine", () ->
			EntityType.Builder.of(HardHerobrine::new, MobCategory.CREATURE)
					.sized(1F, 2F)
					.build(new ResourceLocation(HerobrineMod.MODID, "hard_herobrine").toString()));


	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}
}
