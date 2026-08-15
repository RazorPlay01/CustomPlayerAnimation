package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.Util;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class OnFenceWalkAnimation implements ICustomAnimation {
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
		} else {
			context.mainAnimationContainer().setAnimationSpeed(Util.getAnimationSpeedMultiplier(1, context, CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig));
			context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.getFadeTime());
			context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.onFenceWalkAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.ON_FENCE_WALK_ANIMATION);
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return context.playerData().getMovementSpeed() > 0
				&& !context.playerData().isMovingBackwards() &&
				!context.player().isCrouching()
				&& context.playerData().isOnFence()
				&& !context.player().isPassenger();
	}
}
