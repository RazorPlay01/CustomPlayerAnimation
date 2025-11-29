package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class JumpAnimation implements ICustomAnimation {
	@Override
	public void playAnimation(AnimationContext context) {
		if (!CONFIG.getMainAnimations().extraAnimations.jumpingAnimationsConfig.isEnabled()) return;

		configureAnimationContainer(CONFIG.getMainAnimations().extraAnimations.jumpingAnimationsConfig, context.mainAnimationContainer());

		context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.JUMP_ANIMATION.getAnimationId()));
		context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.JUMP_ANIMATION.getAnimationId());
	}

	@Override
	public boolean shouldPlayAnimation(AnimationContext context) {
		return !context.player().onGround() && context.playerData().isPrevOnGround() && context.playerData().getVectorY() > 0;
	}
}
