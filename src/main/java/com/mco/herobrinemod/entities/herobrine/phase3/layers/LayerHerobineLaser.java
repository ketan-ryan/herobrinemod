package com.mco.herobrinemod.entities.herobrine.phase3.layers;

import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase3.ModelHardestHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase3.RenderHardestHerobrine;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class LayerHerobineLaser implements LayerRenderer<EntityHardestHerobrine>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("herobrinemod:textures/entities/laser.png");
    private final ModelHardestHerobrine modelHerobrine = new ModelHardestHerobrine();
    private final ModelLaser modelLaser = new ModelLaser();
    private RenderHardestHerobrine renderHerobrine;

    public LayerHerobineLaser(RenderHardestHerobrine rH){
        this.renderHerobrine = rH;
    }

    public void doRenderLayer(EntityHardestHerobrine entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        renderHerobrine.bindTexture(TEXTURE);
        modelHerobrine.Head.addChild(modelLaser.box);
        modelHerobrine.setModelAttributes(renderHerobrine.getMainModel());
        modelHerobrine.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
      //  modelHerobrine.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
