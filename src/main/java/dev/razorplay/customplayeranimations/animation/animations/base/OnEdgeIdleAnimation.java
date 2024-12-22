package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class OnEdgeIdleAnimation {
    private OnEdgeIdleAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() == 0 && context.playerData().getBodyYawDelta() == 0 && !context.player().isCrouching() && context.playerData().isOnEdge()) {
            if (!CONFIG.onEdgeAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.onEdgeAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.onEdgeAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.onEdgeAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.ON_EDGE_IDLE_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.ON_EDGE_IDLE_ANIMATION.getAnimationId());
            }
        }
    }
}
