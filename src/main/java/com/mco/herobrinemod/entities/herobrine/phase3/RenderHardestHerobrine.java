package com.mco.herobrinemod.entities.herobrine.phase3;

import com.mco.herobrinemod.entities.herobrine.phase1.ModelHerobrine;
import com.mco.herobrinemod.entities.util.AdvancedLibLayerHeldItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHardestHerobrine extends RenderLiving<EntityHardestHerobrine>
{
    private static ResourceLocation textures = new ResourceLocation("herobrinemod:textures/entities/herobrine.png");
    public static final RenderHardestHerobrine.Factory FACTORY =  new RenderHardestHerobrine.Factory();
    private float f = 0.0F;

    public RenderHardestHerobrine(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHardestHerobrine(), 0.5F);
        this.addLayer(new AdvancedLibLayerHeldItem(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityHardestHerobrine entity)
    {
        return textures;
    }

    @Override
    protected void preRenderCallback(EntityHardestHerobrine entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        GlStateManager.scale(30F, 30F, 30F);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityHardestHerobrine herobrine, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(herobrine, x, y, z, entityYaw, partialTicks);
    }

    public static class Factory implements IRenderFactory<EntityHardestHerobrine>
    {
        @Override
        public Render<? super EntityHardestHerobrine> createRenderFor(RenderManager manager)
        {
            return new RenderHardestHerobrine(manager);
        }
    }
}
