package com.mco.herobrinemod.entities.herobrine.phase2.ai;

import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
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

    public AIShootFireballs(EntityHardHerobrine herobrine, Animation animation){
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

        if(target != null)
        {
            if (herobrine.getAnimationTick() < 10)
                herobrine.faceEntity(target, 30, 30);

            if (herobrine.getAnimationTick() > 10 && herobrine.getAnimationTick() < 60 && herobrine.getAnimationTick() % 5 == 0)
                shootFireball(target);
        }
    }

    private void shootFireball(EntityLivingBase entitylivingbase){
        double d1 = 4.0D;
        Vec3d vec3d = this.herobrine.getLook(1.0F);
        double d2 = entitylivingbase.posX - (this.herobrine.posX + vec3d.x * 4.0D);
        double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.herobrine.posY + (double)(this.herobrine.height / 2.0F));
        double d4 = entitylivingbase.posZ - (this.herobrine.posZ + vec3d.z * 4.0D);
        herobrine.world.playEvent((EntityPlayer)null, 1016, new BlockPos(this.herobrine), 0);
        EntityLargeFireball entitylargefireball = new EntityLargeFireball(herobrine.world, this.herobrine, d2, d3, d4);
        entitylargefireball.explosionPower = (int)herobrine.getScale();
        entitylargefireball.posX = this.herobrine.posX + vec3d.x * herobrine.getScale();
        if(herobrine.getScale() == 6)
            entitylargefireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) + 1.0D;
        else if (herobrine.getScale() == 3)
            entitylargefireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) - 2.0D;
        else if (herobrine.getScale() == 1.5)
            entitylargefireball.posY = herobrine.posY + (double)(herobrine.height / 2.0F) - 5.0D;
        entitylargefireball.posZ = 2 + this.herobrine.posZ + vec3d.z * 4.0D;
        herobrine.world.spawnEntity(entitylargefireball);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        herobrine.currentAnim = null;
    }
}
