package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.entities.herobrine.ModelHumanoid;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;

public class ModelHerobrine extends ModelHumanoid {

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {

        EntityHerobrine herobrine = (EntityHerobrine)entity;
        animate(herobrine, f, f1, f2, f3, f4, f5);

        this.Chest.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {

        EntityHerobrine herobrine = (EntityHerobrine) entity;
        animator.update(herobrine);
        setRotationAngles(f, f1, f2, f3, f4, f5, herobrine);

        //Death

        animator.setAnimation(herobrine.ANIMATION_DEATH);
        animator.startKeyframe(30);
            animator.rotate(Chest, 1.75F, 0F, 0F);
            animator.rotate(Chest, -0.5F, 0F, 0F);
            animator.move(Chest, 0F, 8F, 0F);

            animator.rotate(RLegA, -1.7F, 0F, 0F);
            animator.rotate(RLegB, .5F, 0F, 0F);

            animator.rotate(LLegA, -1.0F, 0F, 0F);
            animator.rotate(LLegB, .7F, 0F, 0F);

            animator.rotate(LArmA, -1.2F, 0F, 0F);

            animator.rotate(RArmB, -.4F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(30);
        animator.startKeyframe(45);
            animator.rotate(Head, -1.1F, 0F, 0F);

            animator.rotate(Chest, 1.75F, 0F, 0F);
            animator.rotate(Chest, -0.5F, 0F, 0F);
            animator.move(Chest, 0F, 8F, 0F);

            animator.rotate(RLegA, -1.7F, 0F, 0F);
            animator.rotate(RLegB, .5F, 0F, 0F);

            animator.rotate(LLegA, -1.0F, 0F, 0F);
            animator.rotate(LLegB, .7F, 0F, 0F);

            animator.rotate(LArmA, -1.2F, 0F, 0F);

            animator.rotate(RArmB, -.4F, 0F, 0F);
        animator.endKeyframe();
        animator.setStaticKeyframe(30);
        animator.resetKeyframe(30);
    }
}
