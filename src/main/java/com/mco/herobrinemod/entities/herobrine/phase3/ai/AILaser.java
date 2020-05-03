package com.mco.herobrinemod.entities.herobrine.phase3.ai;

import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase3.laser.EntityLaser;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.entity.EntityLivingBase;

public class AILaser extends AnimationAI<EntityHardestHerobrine>
{
    private Animation animation;
    private EntityLivingBase target;
    protected EntityHardestHerobrine herobrine;
    private EntityLaser laser;

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
    public void updateTask() {
        super.updateTask();
        if(!herobrine.world.isRemote && herobrine.getAnimationTick() == 1) {
            laser = new EntityLaser(herobrine.world, herobrine, herobrine.posX, herobrine.getEyeHeight() + herobrine.posY,
                    herobrine.posZ, (float) (herobrine.rotationYawHead * Math.PI / 180), (float) (-herobrine.rotationPitch * Math.PI / 180),
                    200);
            herobrine.world.spawnEntity(laser);
        }
        if(herobrine.getAnimationTick() >= 1){
            double x = herobrine.posX;
            double y = herobrine.posY + herobrine.getEyeHeight();
            double z = herobrine.posZ;
            laser.setPosition(x, y, z);

            float yaw = herobrine.rotationYawHead;
            float pitch = -herobrine.rotationPitch;
            laser.setYaw((float)(yaw * Math.PI / 180));
            laser.setPitch((float)(pitch * Math.PI / 180));

            herobrine.rotationPitch += 10;
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }
}
