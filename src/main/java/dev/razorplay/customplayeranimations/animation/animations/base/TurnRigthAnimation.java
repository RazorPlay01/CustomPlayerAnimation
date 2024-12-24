package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class TurnRigthAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!shouldPlayAnimation(context)) {
            return;
        }

        if (!CONFIG.idleAnimations.turnAnimations.turningStandingAnimationConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            return;
        }

        handleTurningAnimation(context);
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getBodyYawDelta() != 0 && !context.player().isCrouching() && context.playerData().getBodyYawDelta() > 0 && !context.player().isPassenger();
    }

    private static void handleTurningAnimation(AnimationContext context) {
        configureAnimationContainer(CONFIG.idleAnimations.turnAnimations.turningStandingAnimationConfig, context.mainAnimationContainer());
        context.mainAnimationContainer().setAnimationSpeed(calculateAnimationSpeed(context));

        context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TURN_RIGHT_ANIMATION.getAnimationId()));
        context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.TURN_RIGHT_ANIMATION.getAnimationId());
    }

    private static float calculateAnimationSpeed(AnimationContext context) {
        float bodyYawDelta = context.playerData().getBodyYawDelta();
        float halfBodyYawDelta = (float) 1 / 2 * bodyYawDelta;
        float speedMultiplier = CONFIG.idleAnimations.turnAnimations.turningStandingAnimationConfig.getSpeedMultiplier();

        if (halfBodyYawDelta > 2 || halfBodyYawDelta < -2) {
            return speedMultiplier;
        }

        return Math.abs(halfBodyYawDelta * speedMultiplier);
    }
}
