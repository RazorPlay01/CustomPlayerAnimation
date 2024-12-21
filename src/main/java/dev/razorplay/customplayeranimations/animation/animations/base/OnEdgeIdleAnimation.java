package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.ON_EDGE_IDLE_ANIMATION;

public class OnEdgeIdleAnimation {
    private OnEdgeIdleAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() == 0 && context.playerData().getBodyYawDelta() == 0 && !context.player().isCrouching()) {
            if (!CONFIG.onEdgeAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                if (CONFIG.onEdgeAnimationConfig.isEnabled() && context.playerData().isOnEdge()) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.onEdgeAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.onEdgeAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.onEdgeAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(ON_EDGE_IDLE_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(ON_EDGE_IDLE_ANIMATION.animationId());
                }
            }
        }
    }
}
