package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.main.HerobrineDamageSources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class EntityAIHerobrineAttack extends EntityAIAttackMelee {

    private final EntityHerobrine herobrine;
    private int raiseArmTicks;

    public EntityAIHerobrineAttack(EntityHerobrine herobrine, double speedIn, boolean longMemoryIn)
    {
        super(herobrine, speedIn, longMemoryIn);
        this.herobrine = herobrine;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        super.resetTask();
        this.herobrine.setAttacking(false);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        super.updateTask();
        ++this.raiseArmTicks;

        if (this.raiseArmTicks >= 5 && this.attackTick <= 7)
        {
            this.herobrine.setAttacking(true);
        }
        else
        {
            this.herobrine.setAttacking(false);
        }
    }

    @Override
    protected void checkAndPerformAttack(EntityLivingBase enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);

        if (distToEnemySqr <= d0 && this.attackTick <= 0)
        {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            enemy.attackEntityFrom(HerobrineDamageSources.HARD_HEROBRINE, 4F);
        }
    }
}
