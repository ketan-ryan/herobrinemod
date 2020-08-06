package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.main.HerobrineDamageSources;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.entity.EntityLivingBase;

public class AISwordSlice extends AnimationAI<EntityHardHerobrine>
{
    private Animation animation;
    private EntityLivingBase target;
    protected EntityHardHerobrine herobrine;

    public AISwordSlice(EntityHardHerobrine herobrine, Animation animation){
        super(herobrine);
        setMutexBits(8);
        this.herobrine = herobrine;
        this.animation = animation;
        this.target = null;
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();

        herobrine.currentAnim = this;
        target = herobrine.getAttackTarget();
    }

    @Override
    public void updateTask()
    {
        super.updateTask();

        if(target != null){
            if(herobrine.getDistance(target) <= herobrine.getScale()*2 && herobrine.getAnimationTick() == 21)
                target.attackEntityFrom(HerobrineDamageSources.HARD_SWORD, 7F);
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        herobrine.currentAnim = null;
    }
}
