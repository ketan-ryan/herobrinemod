package com.mco.herobrinemod.entities.herobrine.phase2;

import com.mco.herobrinemod.entities.herobrine.ModelHumanoid;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelHardHerobrine extends ModelHumanoid {

    @Override
    public void translateToHand()
    {
        GlStateManager.rotate(-10F, 1, 0, 0);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityHardHerobrine herobrine = (EntityHardHerobrine)entity;
        animate(herobrine, f, f1, f2, f3, f4, f5);

        this.Chest.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityHardHerobrine herobrine = (EntityHardHerobrine) entity;
        animator.update(herobrine);
        setRotationAngles(f, f1, f2, f3, f4, f5, herobrine);

        animator.setAnimation(herobrine.ANIMATION_DEATH_FULL);
        animator.startKeyframe(50);
            animator.rotate(Head, -.75F, 0F, 0F);

            animator.rotate(LArmA, -1F, 0.5F, 0F);
            animator.rotate(LArmB, -1F, 0F, 0F);

            animator.rotate(RArmA, -1F, -0.5F, 0F);
            animator.rotate(RArmB, -1F, 0F, 0F);
        animator.endKeyframe();
        animator.startKeyframe(30);
            animator.rotate(Head, -.75F, 0F, 0F);

            animator.rotate(LArmA, -1F, 0.5F, 0F);
            animator.rotate(LArmB, -1F, 0F, 0F);

            animator.rotate(RArmA, -1F, -0.5F, 0F);
            animator.rotate(RArmB, -1F, 0F, 0F);

            animator.rotate(Chest, 0.25F, 0F, 0F);
            animator.move(Chest, 0F, 6F, 0F);

            animator.rotate(LLegA, -0.5F, 0F, 0F);
            animator.rotate(LLegB, 1.5F, 0F, 0F);
            animator.rotate(RLegA, -0.5F, 0F, 0F);
            animator.rotate(RLegB, 1.5F, 0F, 0F);
        animator.endKeyframe();
        animator.startKeyframe(30);
            animator.rotate(Chest, 1F, 0F, 0F);
            animator.move(Chest, 0F, 12F, 0F);

            animator.rotate(LLegA, -0.5F, 0F, 0F);
            animator.rotate(LLegB, 1.5F, 0F, 0F);
            animator.rotate(RLegA, -0.5F, 0F, 0F);
            animator.rotate(RLegB, 1.5F, 0F, 0F);

            animator.rotate(LArmA, -0.8F, 0F, 0F);
            animator.rotate(RArmA, -0.8F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(30);
        animator.startKeyframe(30);
            animator.rotate(Head, -.75F, 0F, 0F);

            animator.rotate(Chest, 1F, 0F, 0F);
            animator.move(Chest, 0F, 12F, 0F);

            animator.rotate(LLegA, -0.5F, 0F, 0F);
            animator.rotate(LLegB, 1.5F, 0F, 0F);
            animator.rotate(RLegA, -0.5F, 0F, 0F);
            animator.rotate(RLegB, 1.5F, 0F, 0F);

            animator.rotate(LArmA, -0.8F, 0F, 0F);
            animator.rotate(RArmA, -0.8F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(30);
    }
}
