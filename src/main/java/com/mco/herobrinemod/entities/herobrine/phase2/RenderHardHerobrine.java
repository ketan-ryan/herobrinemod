package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.entities.util.AdvancedLibLayerHeldItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHardHerobrine extends RenderLiving<EntityHardHerobrine>{

    private static ResourceLocation textures = new ResourceLocation("herobrinemod:textures/entities/herobrine.png");
    public static final RenderHardHerobrine.Factory FACTORY =  new RenderHardHerobrine.Factory();

    public RenderHardHerobrine(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHardHerobrine(), 0.5F);
        this.addLayer(new AdvancedLibLayerHeldItem(this));
        this.addLayer(new LayerHardEyes<>(this));
        this.addLayer(new LayerRGB<>(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityHardHerobrine entity)
    {
        return textures;
    }

    @Override
    protected void preRenderCallback(EntityHardHerobrine herobrine, float partialTickTime) {
        super.preRenderCallback(herobrine, partialTickTime);
        float scale = herobrine.getScale();
        GlStateManager.scale(scale, scale, scale);
        if(herobrine.getDeathTicks() > 0)
            GlStateManager.scale(4,4,4);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityHardHerobrine herobrine, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(herobrine, x, y, z, entityYaw, partialTicks);
    }

    public static class Factory implements IRenderFactory<EntityHardHerobrine>
    {
        @Override
        public Render<? super EntityHardHerobrine> createRenderFor(RenderManager manager)
        {
            return new RenderHardHerobrine(manager);
        }
    }
}
