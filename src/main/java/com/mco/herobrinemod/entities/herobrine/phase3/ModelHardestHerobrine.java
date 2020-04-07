package com.mco.herobrinemod.entities.herobrine.phase3;

import com.mco.herobrinemod.entities.herobrine.ModelHumanoid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelHardestHerobrine extends ModelHumanoid {

    @Override
    public void translateToHand()
    {
        GlStateManager.rotate(-10F, 1, 0, 0);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityHardestHerobrine herobrine = (EntityHardestHerobrine)entity;

        this.Chest.render(f5);
    }


}
