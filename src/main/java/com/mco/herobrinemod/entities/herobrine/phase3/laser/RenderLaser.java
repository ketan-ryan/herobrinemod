package com.mco.herobrinemod.entities.herobrine.phase3.laser;

import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

public class RenderLaser extends Render<EntityLaser>
{
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public static final RenderLaser.Factory FACTORY =  new RenderLaser.Factory();

    private static final double RADIUS = 1.3;

    public RenderLaser(RenderManager manager){
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLaser entity){
        return TEXTURE_BEACON_BEAM;
    }

    @Override
    public void doRender(EntityLaser laser, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(laser, x, y, z, entityYaw, partialTicks);
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(TEXTURE_BEACON_BEAM);
        EntityHardestHerobrine herobrine = (EntityHardestHerobrine) laser.world.getEntityByID(laser.getHerobrineID());
        if(herobrine != null){}
        if(laser.getStartVec() != null && laser.getEndVec() != null)
        {
            double height = laser.getStartVec().y;
            double length = laser.getStartVec().distanceTo(laser.getEndVec());
            double totalWorldTime = laser.world.getWorldTime();
            double texScale = 1.0D;
            double r = 0.5D;
            renderLaser(length, height, totalWorldTime, partialTicks, texScale, r);
        }
    }


    public void renderLaser(double length, double height, double totalWorldTime, double partialTicks, double textureScale, double radius)
    {
        GlStateManager.pushMatrix();
        GlStateManager.glTexParameteri(3553, 10242, 10497);
        GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        double d0 = totalWorldTime + partialTicks;
        double d1 = height < 0 ? d0 : -d0;
        double d2 = MathHelper.frac(d1 * 0.2D - (double)MathHelper.floor(d1 * 0.1D));
        double d14 = -1.0D + d2;
        double d15 = height * textureScale * (0.5D / radius) + d14;

        buf.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buf.pos(-RADIUS, 0, 0).tex(1, d15).color(255, 255, 255, 255).endVertex();
        buf.pos(-RADIUS, length, 0).tex(1, d14).color(255, 255, 255, 255).endVertex();
        buf.pos(RADIUS, length, 0).tex(0, d14).color(255, 255, 255, 255).endVertex();
        buf.pos(RADIUS, 0, 0).tex(0, d15).color(255, 255, 255, 255).endVertex();

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public static class Factory implements IRenderFactory<EntityLaser>
    {
        @Override
        public Render<? super EntityLaser> createRenderFor(RenderManager manager)
        {
            return new RenderLaser(manager);
        }
    }
}
