package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.config.HerobrineConfig;
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
    private float f = 0.0F;

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
        int ticks = herobrine.getDeathTicks();

        if(ticks < 232) {
            f = 0;
            if (herobrine.getDeathTicks() == 0)
                GlStateManager.scale(scale, scale, scale);

            if (herobrine.getDeathTicks() > 0 && herobrine.getDeathTicks() < 232)
                GlStateManager.scale(6, 6, 6);

            boolean f1 = ticks >= 100 && ticks <= 101;
            boolean f2 = ticks >= 120 && ticks <= 121;
            boolean f3 = ticks >= 140 && ticks <= 141;
            boolean f4 = ticks >= 145 && ticks <= 150 && ticks % 5 == 0;
            boolean f5 = ticks >= 151 && ticks <= 156 && ticks % 4 == 0;
            boolean f6 = ticks >= 156 && ticks <= 162 && ticks % 3 == 0;
            boolean f7 = ticks >= 163 && ticks <= 190 && ticks % 2 == 0;

            boolean shouldPulse = f1 || f2 || f3 || f4 || f5 || f6 || f7;

            if (HerobrineConfig.enableFight && shouldPulse)
                GlStateManager.scale(1.1, 1.1, 1.1);
        }

        if(ticks == 1)
            GlStateManager.scale(6,6,6);

        if (ticks > 232) {
            f += .005;

            if(6 - f > 2.5F)
                GlStateManager.scale(6 - f, 6 - f, 6 - f);
            else
                GlStateManager.scale(2.5F, 2.5F, 2.5F);

            GlStateManager.rotate(2500*f, 0, 1, 0);
        }
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
