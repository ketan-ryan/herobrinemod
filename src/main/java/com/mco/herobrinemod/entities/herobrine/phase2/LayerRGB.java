package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.config.HerobrineConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerRGB<T extends EntityHardHerobrine> implements LayerRenderer<T> {

    private static final ResourceLocation HEROBRINE_GLOW = new ResourceLocation(HerobrineMod.MODID, "textures/entities/rgbrine.png");
    private final RenderHardHerobrine herobrineRenderer;

    public LayerRGB(RenderHardHerobrine renderHerobrine)
    {
        this.herobrineRenderer = renderHerobrine;
    }

    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        int ticks = entitylivingbaseIn.getDeathTicks();

        boolean f1 = ticks >= 100 && ticks <= 101;
        boolean f2 = ticks >= 120 && ticks <= 121;
        boolean f3 = ticks >= 140 && ticks <= 141;

        if((f1 || f2 || f3) && HerobrineConfig.enableFight) {
            this.herobrineRenderer.bindTexture(HEROBRINE_GLOW);
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

            if (entitylivingbaseIn.isInvisible()) {
                GlStateManager.depthMask(false);
            } else {
                GlStateManager.depthMask(true);
            }

            int i = 61680;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.herobrineRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            i = entitylivingbaseIn.getBrightnessForRender();
            j = i % 65536;
            k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            this.herobrineRenderer.setLightmap(entitylivingbaseIn);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }

}
