package com.mco.herobrinemod.entities.herobrine.phase3.ai;

import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.entity.EntityLivingBase;

public class AILaser extends AnimationAI<EntityHardestHerobrine>
{
    private Animation animation;
    private EntityLivingBase target;
    protected EntityHardestHerobrine herobrine;
    private float yaw;

    public AILaser(EntityHardestHerobrine herobrine, Animation animation){
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
        target = entity.getAttackTarget();
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
    }

    @Override
    public void resetTask() {
        super.resetTask();
        herobrine.currentAnim = null;
    }
}
