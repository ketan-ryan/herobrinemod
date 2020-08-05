package com.mco.herobrinemod.entities.herobrine.phase3;

import com.mco.herobrinemod.entities.herobrine.ModelHumanoid;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelHardestHerobrine extends ModelHumanoid {

    @Override
    public void translateToHand()
    {
        GlStateManager.rotate(-10F, 1, 0, 0);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityHardestHerobrine herobrine = (EntityHardestHerobrine)entity;
        animate(herobrine, f, f1, f2, f3, f4, f5);

        this.Chest.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityHardestHerobrine herobrine = (EntityHardestHerobrine)entity;
        animator.update(herobrine);
        setRotationAngles(f, f1, f2, f3, f4, f5, herobrine);

        //Laser
        animator.setAnimation(herobrine.ANIMATION_LASER);
        animator.startKeyframe(10);
            animator.move(Chest, 0, 1, 0);
            animator.rotate(Chest, 0.4F, 0F, 0F);
            animator.rotate(ChestB, -0.2F, 0F, 0F);

            animator.rotate(LLegA, -0.5F, 0.5F, 0F);
            animator.rotate(LLegB, 0.5F, 0F, 0F);

            animator.rotate(RLegA, -0.5F, -0.5F, 0F);
            animator.rotate(RLegB, 0.5F, 0F, 0F);

            animator.rotate(RArmA, 0.5F, 0F, -0.25F);
            animator.rotate(RArmB, -0.75F, 0F, 0F);

            animator.rotate(LArmA, 0.5F, 0F, 0.25F);
            animator.rotate(LArmB, -0.75F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(80);
    }

}
