package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.HerobrineMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class HerobrineModel<T extends Entity> extends DefaultedEntityGeoModel<Herobrine> {
	public HerobrineModel() {
		super(new ResourceLocation(HerobrineMod.MODID, "herobrine"));
	}
}
