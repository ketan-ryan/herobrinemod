package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase3.laser.EntityLaser;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AIShootFireballs extends AnimationAI<EntityHardHerobrine> 
{
    private Animation animation;
    private EntityLivingBase target;
    protected EntityHardHerobrine herobrine;
    private EntityLaser laser;

    public AIShootFireballs(EntityHardHerobrine herobrine, Animation animation){
        super(herobrine);
        setMutexBits(8);
        herobrine = herobrine;
        animation = animation;
        target = herobrine.getAttackTarget();
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

        if(herobrine.getAnimationTick() > 20 && target != null)
        {
            for (int i = 0; i < 5; i++) {
                shootFireballs(target);
            }
        }
    }

    private void shootFireballs(EntityLivingBase target)
    {
        Vec3d vec3d = herobrine.getLook(1.0F);
        double d2 = target.posX - (herobrine.posX + vec3d.x * 4.0D);
        double d3;
        if(herobrine.getScale() == 6)
            d3 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (0.5D + herobrine.posY*2 + (double)(herobrine.height / 2.0F));
        else
            d3 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (0.5D + herobrine.posY*1 + (double)(herobrine.height / 2.0F));

        double d4 = target.posZ - (herobrine.posZ + vec3d.z * 4.0D);
        herobrine.world.playEvent((EntityPlayer)null, 1016, new BlockPos(herobrine), 0);
        EntityLargeFireball entitylargefireball = new EntityLargeFireball(herobrine.world, herobrine, d2, d3, d4);
        entitylargefireball.explosionPower = 1;
        entitylargefireball.posX = herobrine.posX;
        if(herobrine.getScale() == 6)
            entitylargefireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) + 5.0D;
        else if (herobrine.getScale() == 3)
            entitylargefireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) + 0.0D;
        else if (herobrine.getScale() == 1.5)
            entitylargefireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) - 3.0D;
        entitylargefireball.posZ = herobrine.posZ;

        herobrine.world.spawnEntity(entitylargefireball);
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
    }
}
