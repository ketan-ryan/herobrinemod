package com.mco.herobrinemod.entities.herobrine.base;

import com.mco.herobrinemod.HerobrineMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BaseHerobrineModel<T extends Entity> extends DefaultedEntityGeoModel<BaseHerobrine> {
	public BaseHerobrineModel() {
		super(new ResourceLocation(HerobrineMod.MODID, "herobrine"));
	}
}
