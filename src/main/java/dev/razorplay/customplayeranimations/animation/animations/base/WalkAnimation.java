package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.WALK_ANIMATION;

public class WalkAnimation {
    private WalkAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && !context.player().isCrouching()) {
            if (!CONFIG.walkingAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.walkingAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.walkingAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.walkingAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(WALK_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(WALK_ANIMATION.animationId());
            }
        }
    }
}
