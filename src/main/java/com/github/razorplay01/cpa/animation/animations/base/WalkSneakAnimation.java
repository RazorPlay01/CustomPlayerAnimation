package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;

public class WalkSneakAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().moveAnimations.walkingSneakAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        configureWalkSneakAnimation(context);
        setWalkSneakAnimation(context);
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

    private static void setWalkSneakAnimation(AnimationContext context) {
        context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALK_SNEAK_ANIMATION.getAnimationId()));
        context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALK_SNEAK_ANIMATION.getAnimationId());
    }
}
