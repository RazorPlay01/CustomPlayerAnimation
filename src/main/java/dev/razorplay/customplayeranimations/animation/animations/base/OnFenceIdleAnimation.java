package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class OnFenceIdleAnimation {
    private OnFenceIdleAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() == 0 && context.playerData().getBodyYawDelta() == 0 && !context.player().isCrouching() && context.playerData().isOnFence()) {
            if (!CONFIG.onFenceAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.onFenceAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.onFenceAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.onFenceAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ON_FENCE_IDLE_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.ON_FENCE_IDLE_ANIMATION.getAnimationId());
            }
        }
    }
}
