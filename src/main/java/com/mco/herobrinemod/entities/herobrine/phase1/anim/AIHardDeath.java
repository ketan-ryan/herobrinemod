package com.mco.herobrinemod.entities.herobrine.phase1.anim;

import com.mco.herobrinemod.entities.herobrine.phase1.EntityHerobrine;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;

public class AIHardDeath extends AnimationAI<EntityHerobrine>{

    public AIHardDeath(EntityHerobrine hardHerobrine, Animation animation)
    {
        super(hardHerobrine);
        setMutexBits(8);
    }

    @Override
    public Animation getAnimation() {
        return entity.getDeathAnimation();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entity.setAnimationTick(0);
    }

    @Override
    public void updateTask() {
        super.updateTask();
    }
}
