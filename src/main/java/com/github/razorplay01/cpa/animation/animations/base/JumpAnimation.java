package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.config.ClientConfig;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;

public class JumpAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().jumpingAnimationsConfig.isEnabled()) return;

		double movementSpeed = context.playerData().getMovementSpeed();
		boolean isMovingBackwards = context.playerData().isMovingBackwards();
		boolean isSprinting = context.player().isSprinting();

		AnimationsId animationId = AnimationsId.JUMP_IDLE_ANIMATION;

		if (movementSpeed > 0) {
			if (isMovingBackwards) {
				animationId = getAnimationIfEnabled(
						CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpBackwardsAnimationConfig,
						AnimationsId.JUMP_BACKWARDS_ANIMATION
				);
			} else if (isSprinting) {
				animationId = getAnimationIfEnabled(
						CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpRunningAnimationConfig,
						AnimationsId.JUMP_RUNNING_ANIMATION
				);
			} else {
				animationId = getAnimationIfEnabled(
						CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpWalkingAnimationConfig,
						AnimationsId.JUMP_WALKING_ANIMATION
				);
			}
		}

		configureAnimations(context, animationId);
		AnimationContainer.setAnimation(context.mainAnimationContainer(), animationId);
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return !context.player().onGround() &&
				context.playerData().isPrevOnGround() &&
				context.playerData().getVectorY() > 0 &&
				!context.player().isCrouching() &&
				!context.player().isPassenger();
	}

	private static void configureAnimations(AnimationContext context, AnimationsId animationId) {
		switch (animationId) {
			case JUMP_BACKWARDS_ANIMATION ->
					configureAnimationContainer(CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpBackwardsAnimationConfig, context.mainAnimationContainer());
			case JUMP_RUNNING_ANIMATION ->
					configureAnimationContainer(CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpRunningAnimationConfig, context.mainAnimationContainer());
			case JUMP_WALKING_ANIMATION ->
					configureAnimationContainer(CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpWalkingAnimationConfig, context.mainAnimationContainer());
			default ->
					configureAnimationContainer(CONFIG.getMainAnimations().jumpingAnimationsConfig.jumpIdleAnimationConfig, context.mainAnimationContainer());
		}
	}

	private AnimationsId getAnimationIfEnabled(ClientConfig.AnimationConfig animationConfig, AnimationsId specificAnimationId) {
		return animationConfig.isEnabled() ? specificAnimationId : AnimationsId.JUMP_IDLE_ANIMATION;
	}
}
