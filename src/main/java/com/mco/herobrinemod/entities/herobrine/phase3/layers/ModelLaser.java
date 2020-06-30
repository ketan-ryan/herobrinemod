package com.mco.herobrinemod.entities.herobrine.phase3.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLaser extends ModelBase
{
    public ModelRenderer box;

    public int textureWidth = 200;
    public int textureHeight = 16;

    public ModelLaser()
    {
        box = new ModelRenderer(this, 0, 0);
        box.addBox(0, 0, 0, 200, 16, 1);
        box.setRotationPoint(0,0,0);
        box.setTextureSize(textureWidth, textureHeight);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        box.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    }
}
