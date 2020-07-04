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
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector4f;

public class RenderHardestHerobrine extends RenderLiving<EntityHardestHerobrine>
{
    private static ResourceLocation TEXTURE = new ResourceLocation("herobrinemod:textures/entities/herobrine.png");
    public static final ResourceLocation BEAM_TEXTURES = new ResourceLocation("herobrinemod:textures/entities/laser.png");

    private static final double RADIUS = 1.7;

    private static final double HEIGHT = 20;
    private static final double TEXTURE_HEIGHT = 16;
    private static final double TEXTURE_WIDTH = 16;

    private Quaternion axisAngleYaw = new Quaternion();
    private Quaternion axisAnglePitch = new Quaternion();

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

        if (herobrine.getAnimation() == herobrine.ANIMATION_LASER && herobrine.getAnimationTick() < 90
                && herobrine.getStartPos() != null && herobrine.getEndPos() != null)
        {
            int frame = herobrine.getAnimationTick();

            renderLaserTranslated(x, y, z,  herobrine.posX, herobrine.posY, herobrine.posZ,
                    herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    herobrine.getRotationYawHead(), herobrine.rotationPitch, -2.3F, frame);
            renderLaserTranslated(x, y , z, herobrine.posX, herobrine.posY, herobrine.posZ,
                    herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    herobrine.getRotationYawHead(), herobrine.rotationPitch, 2.3F, frame);

        }
        super.doRender(herobrine, x, y, z, entityYaw, partialTicks);
    }

    private void renderLaserTranslated(double x, double y, double z, double newX, double newY,
                                         double newZ, double blockX, double blockY, double blockZ,
                                         double yaw, double pitch, float eyeOff, int frame){
        float diffX = (float) (blockX - newX);
        float diffY = (float) (blockY - newY);
        float diffZ = (float) (blockZ - newZ);
        float length = MathHelper.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

        double minU = 0;
        double minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT ;
        double maxU = minU + 16 / TEXTURE_WIDTH ;
        double maxV = minV  / TEXTURE_HEIGHT * frame;

        GlStateManager.pushMatrix();

        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        //Put entity at right place on screen
        GlStateManager.translate(x, y, z);

        //Initialize pitch and yaw quats from axisangle vecs
        axisAngleYaw.setFromAxisAngle(new Vector4f(0, -1, 0, (float)Math.toRadians(yaw)));
        axisAnglePitch.setFromAxisAngle(new Vector4f(1, 0, 0, (float)Math.toRadians(pitch)));

        //Combine quats by multiplying in this specific order
        Quaternion rotQuat = Quaternion.mul(axisAngleYaw, axisAnglePitch, null);

        GlStateManager.rotate(rotQuat);

        //Offset to eyes AFTER rotating
        GlStateManager.translate(eyeOff,41.5, -20);

        //The quad is just a square so scale to proper rectangle length
        GlStateManager.scale(RADIUS, 1, length * 1.5);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();

        //Draw a square
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buf.pos(-1, 0, 0).tex(minU, maxV).endVertex();
        buf.pos(-1, 0, 1).tex(minU, minV).endVertex();
        buf.pos(1, 0, 1).tex(maxU, minV).endVertex();
        buf.pos(1, 0, 0).tex(maxU, maxV).endVertex();

        tessellator.draw();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);

        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

}