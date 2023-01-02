package com.mco.herobrinemod.entities.herobrine.phase1;

import com.mco.herobrinemod.client.ClientAnimationInfoData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class DelayedMeleeAttackGoal extends MeleeAttackGoal {
	public DelayedMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
		super(mob, speedModifier, followingTargetEvenIfNotSeen);
	}

	@Override
	public void tick() {
		super.tick();
		this.mob.setSprinting(this.mob.getNavigation().getPath() != null);
		if(this.mob instanceof Herobrine herobrine) {
			if(herobrine.swingTime == herobrine.damageTime && herobrine.getTarget() != null) {
				double d0 = this.getAttackReachSqr(herobrine.getTarget());
				if(this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(herobrine.getTarget()) <= d0)
					herobrine.doHurtTarget(herobrine.getTarget());
			}
		}
	}

	/**
	 * Override to allow us to control when damage is dealt
	 * @param entity the attack target
	 * @param dist the distance to the target
	 */
	@Override
	protected void checkAndPerformAttack(LivingEntity entity, double dist) {
		if(this.mob instanceof Herobrine herobrine) {
			double d0 = this.getAttackReachSqr(entity);
			if (dist <= d0 && this.getTicksUntilNextAttack() <= 0) {
				this.resetAttackCooldown();
				herobrine.currentState = Herobrine.State.SWING;
				herobrine.swing(InteractionHand.MAIN_HAND);
			}
		}
	}
}
