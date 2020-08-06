package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.EnumParticleTypes;

import java.util.Random;

public class AISummonLightning extends AnimationAI<EntityHardHerobrine>
{
    private Animation animation;
    private EntityLivingBase target;
    protected EntityHardHerobrine herobrine;

    Random random = new Random();

    public AISummonLightning(EntityHardHerobrine herobrine, Animation animation){
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
    public void updateTask() {
        super.updateTask();

        if(target != null && herobrine.getAnimationTick() > 60)
        {
            EntityLightningBolt lightningBolt = new EntityLightningBolt(herobrine.world, target.posX, target.posY, target.posZ, false);

            for(int i = 0; i < random.nextInt(5); i++)
                herobrine.world.addWeatherEffect(lightningBolt);
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        herobrine.currentAnim = null;
    }
}
