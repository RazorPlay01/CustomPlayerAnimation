package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.Util;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class WalkAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
		} else {
			context.mainAnimationContainer().setAnimationSpeed(Util.getAnimationSpeedMultiplier(1, context, CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig));
			context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.getFadeTime());
			context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.walkingAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.WALK_ANIMATION);
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return context.playerData().getMovementSpeed() > 0 &&
				!context.playerData().isMovingBackwards() &&
				!context.player().isCrouching() &&
				!context.player().isPassenger();
	}
}
