package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.animation.AnimationContainer;
import com.github.razorplay01.cpa.util.Util;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;

public class WalkSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().moveAnimations.walkingSneakAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        configureWalkSneakAnimation(context);
		AnimationContainer.setAnimation(context.mainAnimationContainer(), AnimationsId.WALK_SNEAK_ANIMATION);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getMovementSpeed() > 0
                && !context.playerData().isMovingBackwards()
                && context.player().isCrouching() && !context.player().isPassenger();
    }

    private static void configureWalkSneakAnimation(AnimationContext context) {
        var animationContainer = context.mainAnimationContainer();
        var config = CONFIG.getMainAnimations().moveAnimations.walkingSneakAnimationConfig;

		context.mainAnimationContainer().setAnimationSpeed(Util.getAnimationSpeedMultiplier(5, context, config));
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }
}
