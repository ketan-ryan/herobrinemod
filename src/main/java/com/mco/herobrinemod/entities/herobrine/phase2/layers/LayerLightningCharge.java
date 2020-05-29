package com.mco.herobrinemod.entities.herobrine.phase2.layers;

import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.ModelHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.RenderHardHerobrine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerLightningCharge implements LayerRenderer<EntityHardHerobrine>
{
    private static final ResourceLocation CHARGE_TEXTURE = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private final RenderHardHerobrine herobrineRenderer;
    private final ModelHardHerobrine creeperModel = new ModelHardHerobrine();

    public LayerLightningCharge(RenderHardHerobrine herobrineRenderer)
    {
        this.herobrineRenderer = herobrineRenderer;
    }

    public void doRenderLayer(EntityHardHerobrine entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entitylivingbaseIn.getAnimation().equals(entitylivingbaseIn.ANIMATION_LIGHTNING))
        {
            boolean flag = entitylivingbaseIn.isInvisible();
            GlStateManager.depthMask(!flag);
            this.herobrineRenderer.bindTexture(CHARGE_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            float alpha = entitylivingbaseIn.getAnimationTick() < 65 ? 1.0F : 0.0F;
            GlStateManager.translate(f * 0.005F, f * 0.005F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F, alpha);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.creeperModel.setModelAttributes(this.herobrineRenderer.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
