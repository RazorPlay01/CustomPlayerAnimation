package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.TURN_LEFT_ANIMATION;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.TURN_RIGHT_ANIMATION;
import static java.lang.Math.abs;

public class TurnAnimation {
    private TurnAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getBodyYawDelta() != 0 && !context.player().isCrouching()) {
            if (!CONFIG.turningStandingAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setCurrentAnimation((context.playerData().getBodyYawDelta() < 0) ? TURN_LEFT_ANIMATION.animation() : TURN_RIGHT_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId((context.playerData().getBodyYawDelta() < 0) ? TURN_LEFT_ANIMATION.animationId() : TURN_RIGHT_ANIMATION.animationId());

                if ((((float) 1 / 2) * context.playerData().getBodyYawDelta()) > 2 || (((float) 1 / 2) * context.playerData().getBodyYawDelta()) < 2) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.turningStandingAnimationConfig.getSpeedMultiplier());
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(abs((((float) 1 / 2) * context.playerData().getBodyYawDelta()) * CONFIG.turningStandingAnimationConfig.getSpeedMultiplier()));
                }
            }

            context.mainAnimationContainer().setAnimationFadeTime(CONFIG.turningStandingAnimationConfig.getFadeTime());
            context.mainAnimationContainer().setAnimationPriority(CONFIG.turningStandingAnimationConfig.getPriority());
        }
    }
}
