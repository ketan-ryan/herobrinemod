package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.math.MathHelper;

public class AIBreatheFire extends AnimationAI<EntityHardHerobrine> 
{
    private Animation animation;
    private EntityLivingBase target;
    protected EntityHardHerobrine herobrine;

    public AIBreatheFire(EntityHardHerobrine herobrine, Animation animation){
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

    public void updateTask()
    {
        super.updateTask();

        if(target != null)
        {
            if(herobrine.getAnimationTick() < 20)
                herobrine.faceEntity(target, 30F, 30F);

            else
                breatheFire(target);
        }
    }
    
    private void breatheFire(EntityLivingBase target)
    {
        double d0 = herobrine.getDistanceSq(target);
        double d1 = target.posX - herobrine.posX;
        double d2;
        if(herobrine.getScale() == 6)
            d2 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (herobrine.posY*2 + (double)(herobrine.height / 2.0F));
        else
            d2 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (herobrine.posY + (double)(herobrine.height / 2.0F));
        double d3 = target.posZ - herobrine.posZ;
        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.05F;

        for (int i = 0; i < 3; ++i)
        {
            EntitySmallFireball entitysmallfireball = new EntitySmallFireball(herobrine.world, herobrine, d1 + herobrine.getRNG().nextGaussian() * (double)f, d2, d3 + herobrine.getRNG().nextGaussian() * (double)f);
            if(herobrine.getScale() == 6)
                entitysmallfireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) + 3.5D;
            else if (herobrine.getScale() == 3)
                entitysmallfireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) + 0.0D;
            else if (herobrine.getScale() == 1.5)
                entitysmallfireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) - 3.0D;
            herobrine.world.spawnEntity(entitysmallfireball);
        }
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        herobrine.currentAnim = null;
    }
}
