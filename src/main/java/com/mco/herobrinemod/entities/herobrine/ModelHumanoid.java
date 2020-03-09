package com.mco.herobrinemod.entities.herobrine;

import com.mco.herobrinemod.entities.util.AdvancedLibModelBase;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelHumanoid extends AdvancedLibModelBase {

    public AdvancedModelRenderer Chest;
    public AdvancedModelRenderer RArmA;
    public AdvancedModelRenderer Head;
    public AdvancedModelRenderer LArmA;
    public AdvancedModelRenderer ChestB;
    public AdvancedModelRenderer RArmB;
    public AdvancedModelRenderer LArmB;
    public AdvancedModelRenderer LLegA;
    public AdvancedModelRenderer RLegA;
    public AdvancedModelRenderer LLegB;
    public AdvancedModelRenderer RLegB;

    protected ModelAnimator animator;

    public ModelHumanoid() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.LLegB = new AdvancedModelRenderer(this, 0, 20);
        this.LLegB.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.LLegB.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.RLegB = new AdvancedModelRenderer(this, 16, 52);
        this.RLegB.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.RLegB.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.Chest = new AdvancedModelRenderer(this, 16, 16);
        this.Chest.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Chest.addBox(-4.0F, 0.0F, -2.0F, 8, 6, 4, 0.0F);
        this.RArmA = new AdvancedModelRenderer(this, 32, 48);
        this.RArmA.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.RArmA.addBox(-1.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.RLegA = new AdvancedModelRenderer(this, 16, 48);
        this.RLegA.setRotationPoint(-2.1F, 6.0F, 0.0F);
        this.RLegA.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(RLegA, 0.0F, 3.096039560112741F, 0.0F);
        this.LLegA = new AdvancedModelRenderer(this, 0, 16);
        this.LLegA.setRotationPoint(2.1F, 6.0F, 0.0F);
        this.LLegA.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.setRotateAngle(LLegA, 0.0F, 3.141592653589793F, 0.0F);
        this.ChestB = new AdvancedModelRenderer(this, 13, 22);
        this.ChestB.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.ChestB.addBox(-4.0F, 0.0F, -2.0F, 8, 6, 4, 0.0F);
        this.setRotateAngle(ChestB, 0.0F, -3.141592653589793F, 0.0F);
        this.LArmA = new AdvancedModelRenderer(this, 32, 48);
        this.LArmA.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.LArmA.addBox(-3.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.LArmB = new AdvancedModelRenderer(this, 40, 32);
        this.LArmB.setRotationPoint(-1.0F, 5.0F, 0.0F);
        this.LArmB.addBox(-2.0F, 1.0F, -2.0F, 4, 6, 4, 0.0F);
        this.RArmB = new AdvancedModelRenderer(this, 40, 32);
        this.RArmB.setRotationPoint(1.0F, 5.0F, 0.0F);
        this.RArmB.addBox(-2.0F, 1.0F, -2.0F, 4, 6, 4, 0.0F);
        this.Head = new AdvancedModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.LLegA.addChild(this.LLegB);
        this.RLegA.addChild(this.RLegB);
        this.Chest.addChild(this.RArmA);
        this.ChestB.addChild(this.RLegA);
        this.ChestB.addChild(this.LLegA);
        this.Chest.addChild(this.ChestB);
        this.Chest.addChild(this.LArmA);
        this.LArmA.addChild(this.LArmB);
        this.RArmA.addChild(this.RArmB);
        this.Chest.addChild(this.Head);


        animator = ModelAnimator.create();
        updateDefaultPose();
    }

    @Override
    public AdvancedModelRenderer getModelForItem()
    {
        return this.LArmA;
    }

    @Override
    public void translateToHand()
    {
        GlStateManager.translate(0F, 0F, 0F);
        GlStateManager.rotate(-10F, 1, 0, 0);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(AdvancedModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        resetToDefaultPose();

        this.Head.rotateAngleX = headPitch / 50f;
        this.Head.rotateAngleY = netHeadYaw / 50f;

        this.LLegA.rotateAngleX = MathHelper.cos(limbSwing * 0.3262F) * 1.4F * limbSwingAmount;
        this.LLegB.rotateAngleX = Math.abs(MathHelper.cos(limbSwing * 0.2262F)) * 1.4F * limbSwingAmount;

        this.RArmA.rotateAngleX = MathHelper.cos(limbSwing * 0.2662F) * 1.4F * limbSwingAmount;
        this.RArmB.rotateAngleX = Math.abs(MathHelper.cos(limbSwing * 0.0662F)) * -1F * limbSwingAmount;

        this.RLegA.rotateAngleX = MathHelper.cos(limbSwing * 0.3262F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.RLegB.rotateAngleX = Math.abs(MathHelper.cos(limbSwing * 0.2262F + (float)Math.PI)) * 1.4F * limbSwingAmount;

        this.LArmA.rotateAngleX = MathHelper.cos(limbSwing * 0.1662F + (float)Math.PI) * 1.2F * limbSwingAmount;
        this.LArmB.rotateAngleX = Math.abs(MathHelper.cos(limbSwing * 0.0662F + (float)Math.PI)) * -1F * limbSwingAmount;

        this.Chest.rotateAngleX = Math.abs(MathHelper.cos(limbSwing * 0.2662F)) * .5F * limbSwingAmount;

    }
}
