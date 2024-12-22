package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class TurnAnimation {
    private TurnAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (!shouldPlayTurningAnimation(context)) {
            return;
        }

        if (!CONFIG.turningStandingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        handleTurningAnimation(context);
    }

    private static boolean shouldPlayTurningAnimation(AnimationContext context) {
        return context.playerData().getBodyYawDelta() != 0 && !context.player().isCrouching();
    }

    private static void handleTurningAnimation(AnimationContext context) {
        setTurningAnimation(context);
        configureTurningAnimationContainer(context);
    }

    private static void setTurningAnimation(AnimationContext context) {
        float bodyYawDelta = context.playerData().getBodyYawDelta();

        if (bodyYawDelta < 0) {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TURN_LEFT_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.TURN_LEFT_ANIMATION.getAnimationId());
        } else {
            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TURN_RIGHT_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.TURN_RIGHT_ANIMATION.getAnimationId());
        }
    }

    private static void configureTurningAnimationContainer(AnimationContext context) {
        var animationContainer = context.mainAnimationContainer();
        var config = CONFIG.turningStandingAnimationConfig;

        animationContainer.setAnimationSpeed(calculateAnimationSpeed(context));
        animationContainer.setAnimationFadeTime(config.getFadeTime());
        animationContainer.setAnimationPriority(config.getPriority());
    }

    private static float calculateAnimationSpeed(AnimationContext context) {
        float bodyYawDelta = context.playerData().getBodyYawDelta();
        float halfBodyYawDelta = (float) 1 / 2 * bodyYawDelta;
        float speedMultiplier = CONFIG.turningStandingAnimationConfig.getSpeedMultiplier();

        if (halfBodyYawDelta > 2 || halfBodyYawDelta < -2) {
            return speedMultiplier;
        }

        return Math.abs(halfBodyYawDelta * speedMultiplier);
    }
}
