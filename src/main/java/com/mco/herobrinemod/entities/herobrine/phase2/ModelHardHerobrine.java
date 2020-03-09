package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.entities.herobrine.ModelHumanoid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelHardHerobrine extends ModelHumanoid {

    @Override
    public void translateToHand()
    {
        GlStateManager.rotate(-10F, 1, 0, 0);
    }


    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {

        EntityHardHerobrine herobrine = (EntityHardHerobrine)entity;
    //    animate(herobrine, f, f1, f2, f3, f4, f5);

        this.Chest.render(f5);
    }
}
