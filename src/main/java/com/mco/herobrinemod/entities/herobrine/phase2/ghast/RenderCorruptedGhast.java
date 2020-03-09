package com.mco.herobrinemod.entities.herobrine.phase2.ghast;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCorruptedGhast extends RenderLiving<EntityCorruptedGhast> {
    private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation("herobrinemod:textures/entities/corrupted_ghast.png");
    private static final ResourceLocation GHAST_SHOOTING_TEXTURES = new ResourceLocation("herobrinemod:textures/entities/corrupted_ghast_shooting.png");
    public static final RenderCorruptedGhast.Factory FACTORY =  new RenderCorruptedGhast.Factory();

    public RenderCorruptedGhast(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelGhast(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCorruptedGhast entity)
    {
        return entity.isAttacking() ? GHAST_SHOOTING_TEXTURES : GHAST_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityCorruptedGhast entitylivingbaseIn, float partialTickTime)
    {
        float f = 1.0F;
        float f1 = 4.5F;
        float f2 = 4.5F;
        GlStateManager.scale(4.5F, 4.5F, 4.5F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static class Factory implements IRenderFactory<EntityCorruptedGhast>
    {
        @Override
        public Render<? super EntityCorruptedGhast> createRenderFor(RenderManager manager)
        {
            return new RenderCorruptedGhast(manager);
        }
    }
}
