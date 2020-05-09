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

        int ticks = herobrine.getDeathTicks();
        boolean b1 = ticks >= 100 && ticks <= 101;
        boolean b2 = ticks >= 120 && ticks <= 121;
        boolean b3 = ticks >= 140 && ticks <= 141;
        boolean b4 = ticks >= 145 && ticks <= 150 && ticks % 5 == 0;
        boolean b5 = ticks >= 151 && ticks <= 156 && ticks % 4 == 0;
        boolean b6 = ticks >= 156 && ticks <= 162 && ticks % 3 == 0;
        boolean b7 = ticks >= 163 && ticks <= 190 && ticks % 2 == 0;

        boolean shouldPulse = (b1 || b2 || b3 || b4 || b5 || b6 || b7);

        if((herobrine.getAnimation() == herobrine.ANIMATION_DEATH && !shouldPulse) || ticks == 0 || ticks >= 190)
            this.Chest.render(f5);
        else if(herobrine.getAnimation() == herobrine.ANIMATION_DEATH_FULL)
            this.Chest.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        EntityHardHerobrine herobrine = (EntityHardHerobrine) entity;
        animator.update(herobrine);
        setRotationAngles(f, f1, f2, f3, f4, f5, herobrine);

        int ticks = herobrine.getDeathTicks();
        boolean b1 = ticks >= 100 && ticks <= 101;
        boolean b2 = ticks >= 120 && ticks <= 121;
        boolean b3 = ticks >= 140 && ticks <= 141;
        boolean b4 = ticks >= 145 && ticks <= 150 && ticks % 5 == 0;
        boolean b5 = ticks >= 151 && ticks <= 156 && ticks % 4 == 0;
        boolean b6 = ticks >= 156 && ticks <= 162 && ticks % 3 == 0;
        boolean b7 = ticks >= 163 && ticks <= 190 && ticks % 2 == 0;

        boolean shouldPulse = (b1 || b2 || b3 || b4 || b5 || b6 || b7);

        if(herobrine.getAnimation() == herobrine.ANIMATION_DEATH && shouldPulse)
            this.Chest.render(f5);

        animator.setAnimation(herobrine.ANIMATION_DEATH);
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
        animator.startKeyframe(15);
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
        animator.setStaticKeyframe(40);
        animator.resetKeyframe(30);
        animator.startKeyframe(40);
            animator.rotate(Head, 0.75F, 0F, 0F);

            animator.rotate(LArmA, -0.5F, -0.5F, 0F);
            animator.rotate(LArmB, -0.75F, -0.25F, 0F);
            animator.rotate(RArmA, -0.5F, 0.5F, 0F);
            animator.rotate(RArmB, -0.75F, 0.25F, 0F);

            animator.rotate(RLegA, -1.5F, 0F, 0F);
            animator.rotate(RLegB, 1F, -0F, 0F);
            animator.rotate(LLegA, -1.5F, 0F, 0F);
            animator.rotate(LLegB, 1F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(80);
        animator.startKeyframe(5);
            animator.rotate(Head, -0.75F, 0F, 0F);

            animator.rotate(LArmA, 0.5F, -0.5F, 0F);
            animator.rotate(LArmB, 0.75F, -0.25F, 0F);
            animator.rotate(RArmA, 0.5F, 0.5F, 0F);
            animator.rotate(RArmB, 0.75F, 0.25F, 0F);

            animator.rotate(RLegA, 1.5F, 0F, 0F);
            animator.rotate(RLegB, -1F, -0F, 0F);
            animator.rotate(LLegA, 1.5F, 0F, 0F);
            animator.rotate(LLegB, -1F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(5);

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

        //Fireball shoot
        animator.setAnimation(herobrine.ANIMATION_SHOOT);
        animator.startKeyframe(20);
            animator.rotate(RArmA, -0.85F, 0F, 0F);
            animator.rotate(RArmB, -0.35F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(20);
    }
}
