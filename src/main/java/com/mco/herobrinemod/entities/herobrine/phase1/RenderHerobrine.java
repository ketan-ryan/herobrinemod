package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.entities.herobrine.LayerHerobrineEyes;
import com.mco.herobrinemod.entities.util.AdvancedLibLayerHeldItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHerobrine extends RenderLiving<EntityHerobrine> {
    
    private static ResourceLocation textures = new ResourceLocation("herobrinemod:textures/entities/herobrine.png");
    public static final Factory FACTORY =  new Factory();

    public RenderHerobrine(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHerobrine(), 0.5F);
        this.addLayer(new AdvancedLibLayerHeldItem(this));
        this.addLayer(new LayerHerobrineEyes<>(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityHerobrine entity)
    {
        return textures;
    }

    @Override
    protected void preRenderCallback(EntityHerobrine herobrine, float partialTickTime) {
        super.preRenderCallback(herobrine, partialTickTime);
        if (herobrine.deathTicks >= 95) {
            GlStateManager.scale(1.5F, 1.5F, 1.5F);
            GlStateManager.translate(0F, .2F, 0F);
        }
        if (herobrine.deathTicks >= 105)
            GlStateManager.scale(2.5F, 2.5F, 2.5F);
        if (herobrine.deathTicks >= 115)
            GlStateManager.scale(1.6F, 1.6F, 1.6F);
        if(herobrine.deathTicks >= 145)
            GlStateManager.translate(0F, -.1F, 0F);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityHerobrine herobrine, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(herobrine, x, y, z, entityYaw, partialTicks);
    }

    public static class Factory implements IRenderFactory<EntityHerobrine>
    {
        @Override
        public Render<? super EntityHerobrine> createRenderFor(RenderManager manager)
        {
            return new RenderHerobrine(manager);
        }
    }
}
