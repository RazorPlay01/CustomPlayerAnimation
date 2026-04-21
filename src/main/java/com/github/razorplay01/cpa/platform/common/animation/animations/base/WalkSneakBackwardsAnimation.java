package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.Util;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class WalkSneakBackwardsAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().moveAnimations.walkingSneakBackwardsAnimationConfig.isEnabled()) {
			context.mainAnimationContainer().disableAnimation();
		} else {
			context.mainAnimationContainer().setAnimationSpeed(Util.getAnimationSpeedMultiplier(5, context, CONFIG.getMainAnimations().moveAnimations.walkingSneakBackwardsAnimationConfig));
			context.mainAnimationContainer().setAnimationFadeTime(CONFIG.getMainAnimations().moveAnimations.walkingSneakBackwardsAnimationConfig.getFadeTime());
			context.mainAnimationContainer().setAnimationPriority(CONFIG.getMainAnimations().moveAnimations.walkingSneakBackwardsAnimationConfig.getPriority());

			AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.WALK_SNEAK_BACKWARDS_ANIMATION);
		}
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards() && context.player().isCrouching() && !context.player().isPassenger();
	}
}
