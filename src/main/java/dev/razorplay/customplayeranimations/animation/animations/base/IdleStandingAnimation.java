package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.IDLE_STANDING_ANIMATION;

public class IdleStandingAnimation {
    private IdleStandingAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() == 0 && context.playerData().getBodyYawDelta() == 0 && !context.player().isCrouching()) {
            if (!CONFIG.idleStandingAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.idleStandingAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.idleStandingAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.idleStandingAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(IDLE_STANDING_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(IDLE_STANDING_ANIMATION.animationId());
            }
        }
    }
}
