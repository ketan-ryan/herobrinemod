package com.mco.herobrinemod.entities.herobrine.phase3;

import com.mco.herobrinemod.entities.util.AdvancedLibLayerHeldItem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

public class RenderHardestHerobrine extends RenderLiving<EntityHardestHerobrine>
{
    private static ResourceLocation TEXTURE = new ResourceLocation("herobrinemod:textures/entities/herobrine.png");
    public static final ResourceLocation BEAM_TEXTURES = new ResourceLocation("textures/entity/beacon_beam.png");
    public static final ResourceLocation ENDERCRYSTAL_BEAM_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");

    private static final double RADIUS = 1.3;
    private static final double HEIGHT = 20;

    public static final RenderHardestHerobrine.Factory FACTORY =  new RenderHardestHerobrine.Factory();
    private float f = 0.0F;

    public RenderHardestHerobrine(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHardestHerobrine(), 0.5F);
        this.addLayer(new AdvancedLibLayerHeldItem(this));
       // this.addLayer(new LayerHerobineLaser());
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityHardestHerobrine entity)
    {
        return TEXTURE;
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
        GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(BEAM_TEXTURES);
        if(herobrine.getStartPos() != null && herobrine.getEndPos() != null)
        {
            double height = herobrine.getStartPos().y;
            double length = herobrine.getStartPos().distanceTo(herobrine.getEndPos());
            double totalWorldTime = herobrine.world.getWorldTime();
            double texScale = 10.0D;
            double r = 10.5D;
//            renderLaser(length, height, totalWorldTime, partialTicks * 10, texScale, r, x, y, z, herobrine.getEndPos().x,
//                    herobrine.getEndPos().y, herobrine.getEndPos().z);
            renderLaser2(x, y, z, partialTicks, herobrine.posX + (herobrine.prevPosX - herobrine.posX) * (double)(1.0F - partialTicks),
                    herobrine.posY + (herobrine.prevPosY - herobrine.posY) * (double)(1.0F - partialTicks),
                    herobrine.posZ + (herobrine.prevPosZ - herobrine.posZ) * (double)(1.0F - partialTicks),
                    herobrine.ticksExisted, herobrine.getEndPos().x, (double)f + herobrine.getEndPos().y,
                    herobrine.getEndPos().z, height, texScale, r, herobrine.renderYawOffset, herobrine.rotationPitch);
         //   renderLaser3(x, y, z, 0, 1, 0, 1, 5, 45, length, 20);
        }
    }

    private void renderLaser3(double x, double y, double z,float minU, float maxU, float minV, float maxV,
                              double x_size, double y_size, double z_size, double scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        bufferBuilder.pos(-x_size + x, y_size + y, -z_size + z).tex(maxU, maxV).endVertex();
        bufferBuilder.pos(-x_size + x, y_size + y, z_size + z).tex(maxU, minV).endVertex();
        bufferBuilder.pos(x_size + x, y_size + y, z_size + z).tex(minU, minV).endVertex();
        bufferBuilder.pos(x_size + x, y_size + y, -z_size + z).tex(minU, maxV).endVertex();

        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.popMatrix();
    }

    private void renderLaser2(double x, double y, double z, float partialTicks, double newX, double newY,
                              double newZ, int ticksExisted, double blockX, double blockY, double blockZ, double height,
                              double textureScale, double radius, double yaw, double pitch) {
        float f = (float)(blockX - newX);
        float f1 = (float)(blockY - 1.0D - newY);
        float f2 = (float)(blockZ - newZ);
        float length = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    //    float f5 = 0.0F - ((float)ticksExisted + partialTicks) * 0.01F;
     //   float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - ((float)ticksExisted + partialTicks) * 0.01F;
       /* double d0 = ticksExisted + partialTicks;
        double d1 = height < 0 ? d0 : -d0;
        double d2 = MathHelper.frac(d1 * 0.2D - (double)MathHelper.floor(d1 * 0.1D));
        double d14 = -1.0D + d2;
        double d15 = height * textureScale * (0.5D / radius) + d14;
       */
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x +1, (float)y + 55F, (float)z);
      //  GlStateManager.rotate(65, 0, 0, 1);
     //   GlStateManager.scale(1F, 7F, 1F);
     //   GlStateManager.rotate((float)(13 * Math.PI * yaw)/180, -1, 0, 0);
     //   GlStateManager.rotate((float)pitch, 0, -1, 0);

       // GlStateManager.glTexParameteri(3553, 10242, 10497);
      //  GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-RADIUS + x, height + y, -length + z).tex(0, 0).endVertex();
        buf.pos(-RADIUS + x, height + y, length + z).tex(0, 1).endVertex();
        buf.pos(RADIUS + x, height + y, length + z).tex(1, 0).endVertex();
        buf.pos(RADIUS + x, height + y, -length + z).tex(1, 1).endVertex();
/*        for (int j = 0; j <= 8; ++j)
        {
            float f7 = MathHelper.sin((float)(j % 8) * ((float)Math.PI * 2F) / 2.0F) * 0.75F;
            float f8 = MathHelper.cos((float)(j % 8) * ((float)Math.PI * 2F) / 2.0F) * 0.75F;
            float f9 = (float)(j % 4) / 2.0F;

            //   buf.pos((double)(f7), (double)(f8 * 1F), 0).tex(f9, f5).color(255, 255, 255, 255).endVertex();
         //   buf.pos((double)f7, (double)f8, (double)dist*2).tex(f9, f6).color(255, 255, 255, 255).endVertex();
        }*/

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    private void renderLaser(double length, double height, double totalWorldTime, double partialTicks,
                             double textureScale, double radius, double x, double y, double z, double blockX,
                             double blockY, double blockZ)
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
        double d3 = d0 * 0.025D * -1.5D;
        double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * RADIUS;
        double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * RADIUS;
        double d6 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * RADIUS;
        double d7 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * RADIUS;
        double d14 = -1.0D + d2;
        double d15 = height * textureScale * (0.5D / radius) + d14;

        buf.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buf.pos(-RADIUS, 0, 0).tex(1, d15).color(255, 255, 255, 255).endVertex();
        buf.pos(-RADIUS, 0, 0).tex(1, d14).color(255, 255, 255, 255).endVertex();
        buf.pos(RADIUS, length, 0).tex(0, d14).color(255, 255, 255, 255).endVertex();
        buf.pos(RADIUS, length, 0).tex(0, d15).color(255, 255, 255, 255).endVertex();

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
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
