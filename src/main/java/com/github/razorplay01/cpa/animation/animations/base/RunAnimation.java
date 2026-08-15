package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.Util;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class RunAnimation implements ICustomAnimation {
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().moveAnimations.runningAnimationConfig.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
		} else {
			context.mainAnimationContainer().setAnimationSpeed(Util.getAnimationSpeedMultiplier(1, context, CONFIG.getMainAnimations().moveAnimations.runningAnimationConfig));
			context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.runningAnimationConfig.getFadeTime());
			context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.runningAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.RUN_ANIMATION);
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return context.playerData().getMovementSpeed() > 0 &&
				context.player().isSprinting() &&
				!context.playerData().isMovingBackwards() &&
				!context.player().isCrouching() &&
				!context.player().isPassenger();
	}
}
