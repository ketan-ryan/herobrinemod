package com.mco.herobrinemod.entities.herobrine.base;

import com.mco.herobrinemod.client.ClientAnimationInfoData;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;

import static software.bernie.geckolib.constant.DefaultAnimations.RUN;
import static software.bernie.geckolib.constant.DefaultAnimations.WALK;

public class Animations {
	/**
	 * Generic {@link DefaultAnimations#WALK walk} + {@link DefaultAnimations#RUN run} controller.<br>
	 * If the entity is considered moving, will either walk or run depending on the {@link Entity#isSprinting()} method, otherwise it will stop
	 */
	public static <T extends Entity & GeoAnimatable> AnimationController<T> genericWalkRunController(T entity) {
		return new AnimationController<T>(entity, "Walk/Run", 0, state -> {
			if (state.isMoving()) {
				return state.setAndContinue(entity.isSprinting() ? RUN : WALK);
			}
			else {
				return PlayState.STOP;
			}
		});
	}
}
