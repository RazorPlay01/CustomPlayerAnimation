package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.WALKING_BACKWARDS_ANIMATION;

public class WalkBackwardsAnimation {
    private WalkBackwardsAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards() && !context.player().isCrouching()) {
            if (!CONFIG.walkingBackwardsAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.walkingBackwardsAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.walkingBackwardsAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.walkingBackwardsAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(WALKING_BACKWARDS_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(WALKING_BACKWARDS_ANIMATION.animationId());
            }
        }
    }
}
