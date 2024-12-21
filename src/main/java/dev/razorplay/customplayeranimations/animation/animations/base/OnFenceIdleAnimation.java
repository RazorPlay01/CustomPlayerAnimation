package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.ON_FENCE_IDLE_ANIMATION;

public class OnFenceIdleAnimation {
    private OnFenceIdleAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() == 0 && context.playerData().getBodyYawDelta() == 0 && !context.player().isCrouching()) {
            if (!CONFIG.onFenceAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                if (CONFIG.onFenceAnimationConfig.isEnabled() && context.playerData().isOnFence()) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.onFenceAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.onFenceAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.onFenceAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(ON_FENCE_IDLE_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(ON_FENCE_IDLE_ANIMATION.animationId());
                }

            }
        }
    }
}
