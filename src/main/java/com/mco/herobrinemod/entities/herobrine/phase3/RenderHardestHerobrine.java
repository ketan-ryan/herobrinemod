package com.mco.herobrinemod.entities.herobrine.phase3;

import com.mco.herobrinemod.entities.util.AdvancedLibLayerHeldItem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderHardestHerobrine extends RenderLiving<EntityHardestHerobrine>
{
    private static ResourceLocation TEXTURE = new ResourceLocation("herobrinemod:textures/entities/herobrine.png");
    public static final ResourceLocation BEAM_TEXTURES = new ResourceLocation("herobrinemod:textures/entities/laser.png");

    private static final double RADIUS = 1.7;

    private static final double HEIGHT = 20;
    private static final double TEXTURE_HEIGHT = 16;
    private static final double TEXTURE_WIDTH = 16;

    private float f = 0.0F;


    public RenderHardestHerobrine(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHardestHerobrine(), 0.5F);
        this.addLayer(new AdvancedLibLayerHeldItem(this));
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
        this.bindTexture(BEAM_TEXTURES);
        GlStateManager.alphaFunc(516, 0.1F);

        if (herobrine.getStartPos() != null && herobrine.getEndPos() != null) {
            double height = herobrine.getStartPos().y;
            double totalWorldTime = herobrine.world.getWorldTime();
            double texScale = 10.0D;

            renderLaserTranslated(x - 3.8, y - 0.5, z, partialTicks, herobrine.posX, herobrine.posY, herobrine.posZ,
                    totalWorldTime, herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    height, texScale, herobrine.getRotationYawHead(), herobrine.rotationPitch);
            renderLaserTranslated(x + 3.8, y - 0.5, z, partialTicks, herobrine.posX, herobrine.posY, herobrine.posZ,
                    totalWorldTime, herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    height, texScale, herobrine.getRotationYawHead(), herobrine.rotationPitch);

        }
        super.doRender(herobrine, x, y, z, entityYaw, partialTicks);
    }

    private void renderLaserTranslated(double x, double y, double z, float partialTicks, double newX, double newY,
                                         double newZ, double totalWorldTime, double blockX, double blockY, double blockZ,
                                         double height, double textureScale, double yaw, double pitch){
        float diffX = (float) (blockX - newX);
        float diffY = (float) (blockY - newY);
        float diffZ = (float) (blockZ - newZ);
        float length = MathHelper.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

        double d1 = totalWorldTime + partialTicks;
        double d2 = MathHelper.frac(d1 * 0.2D - (double) MathHelper.floor(d1 * 0.1D));
        double d14 = -1.0D + d2;
        double d15 = (double) height * textureScale * (0.5D / RADIUS) + d14;

        double minU = 0;
        double minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * 1;
        double maxU = minU + 20 / TEXTURE_WIDTH;
        double maxV = minV + 1 / TEXTURE_HEIGHT;

        GlStateManager.pushMatrix();

        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0, height, 0);
        GlStateManager.rotate((float) -yaw, 0, 1, 0);
        GlStateManager.rotate((float) pitch, 1, 0, 0);

        GlStateManager.scale(RADIUS, 1, length * 1.5);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buf.pos(-1, 0, 0).tex(minU, minV).endVertex();
        buf.pos(-1, 0, 1).tex(minU, maxV).endVertex();
        buf.pos(1, 0, 1).tex(maxU, maxV).endVertex();
        buf.pos(1, 0, 0).tex(maxU, minV).endVertex();

        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }}