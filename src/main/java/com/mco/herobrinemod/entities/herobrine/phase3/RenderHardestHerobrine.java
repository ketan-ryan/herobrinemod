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
    public static final ResourceLocation BEAM_TEXTURES = new ResourceLocation("herobrinemod:textures/entities/laser.png");

    private static final double RADIUS = 1.7;

    public static final RenderHardestHerobrine.Factory FACTORY =  new RenderHardestHerobrine.Factory();

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
            renderLaser(x - 3.8, y + 9, z, partialTicks, herobrine.posX, herobrine.posY, herobrine.posZ,
                    herobrine.ticksExisted, herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    height, texScale);

            renderLaser(x + 3.8, y + 9, z, partialTicks, herobrine.posX, herobrine.posY, herobrine.posZ,
                    herobrine.ticksExisted, herobrine.getEndPos().x, herobrine.getEndPos().y, herobrine.getEndPos().z,
                    height, texScale);
        }
    }

    private void renderLaser(double x, double y, double z, float partialTicks, double newX, double newY, double newZ,
                             int ticksExisted, double blockX, double blockY, double blockZ, double height, double textureScale) {

        float f = (float)(blockX - newX);
        float f1 = (float)(blockY - 1.0D - newY);
        float f2 = (float)(blockZ - newZ);
        float length = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) + 5;

        GlStateManager.pushMatrix();
        GlStateManager.glTexParameteri(3553, 10242, 10497);
        GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.enableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        buf.pos(-RADIUS + x, height + y, + z).tex(0, 0).lightmap(0,240).color(255, 255, 255, 255).endVertex();
        buf.pos(-RADIUS + x, height+y, length + z).tex(0, 1).lightmap(0,240).color(255, 255, 255, 255).endVertex();
        buf.pos(RADIUS + x, height+y, length + z).tex(1, 0).lightmap(0, 240).color(255, 255, 255, 255).endVertex();
        buf.pos(RADIUS + x, height + y,  z).tex(1, 1).lightmap(0, 240).color(255, 255, 255, 255).endVertex();

        tessellator.draw();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);
        GlStateManager.disableBlend();
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
