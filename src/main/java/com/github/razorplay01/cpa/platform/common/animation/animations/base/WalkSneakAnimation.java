package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.animation.AnimationContainer;
import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

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

        animationContainer.setAnimationSpeed(calculateAnimationSpeed(context));
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }

    private static float calculateAnimationSpeed(AnimationContext context) {
        return (float) (5 * context.playerData().getMovementSpeed()
                * CONFIG.getMainAnimations().moveAnimations.getAnimationMoveSpeedMultiplier()
                * CONFIG.getMainAnimations().moveAnimations.walkingSneakAnimationConfig.getSpeedMultiplier());
    }
}
