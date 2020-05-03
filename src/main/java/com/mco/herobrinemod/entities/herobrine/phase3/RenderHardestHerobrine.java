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

    private static final double RADIUS = 1.7;
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
            double height = herobrine.getStartPos().y - 10;
            double totalWorldTime = herobrine.world.getWorldTime();
            double texScale = 10.0D;
            double r = 10.5D;
            renderLaser(x - 3.8, y - 0.5, z, partialTicks, herobrine.posX, herobrine.posY, herobrine.posZ,
                    herobrine.ticksExisted, herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    height, texScale, r, herobrine.renderYawOffset, herobrine.rotationPitch);
            renderLaser(x + 3.8, y - 0.5, z, partialTicks, herobrine.posX, herobrine.posY, herobrine.posZ,
                    herobrine.ticksExisted, herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    height, texScale, r, herobrine.renderYawOffset, herobrine.rotationPitch);
        }
    }

    private void renderLaser(double x, double y, double z, float partialTicks, double newX, double newY,
                              double newZ, int ticksExisted, double blockX, double blockY, double blockZ, double height,
                              double textureScale, double radius, double yaw, double pitch) {

        float f = (float)(blockX - newX);
        float f1 = (float)(blockY - 1.0D - newY);
        float f2 = (float)(blockZ - newZ);
        float length = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buf.pos(-RADIUS + x, height + y, + z).tex(0, 0).endVertex();
        buf.pos(-RADIUS + x, height + y, length + z).tex(0, 1).endVertex();
        buf.pos(RADIUS + x, height + y, length + z).tex(1, 0).endVertex();
        buf.pos(RADIUS + x, height + y,  z).tex(1, 1).endVertex();

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
