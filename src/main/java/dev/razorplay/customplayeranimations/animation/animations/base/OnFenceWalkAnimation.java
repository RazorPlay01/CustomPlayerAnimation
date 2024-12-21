package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.ON_FENCE_WALKING_ANIMATION;

public class OnFenceWalkAnimation {
    private OnFenceWalkAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && !context.player().isCrouching() && context.playerData().isOnFence()) {
            if (!CONFIG.onFenceAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.onFenceAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.onFenceAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.onFenceAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(ON_FENCE_WALKING_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(ON_FENCE_WALKING_ANIMATION.animationId());
            }
        }
    }
}
