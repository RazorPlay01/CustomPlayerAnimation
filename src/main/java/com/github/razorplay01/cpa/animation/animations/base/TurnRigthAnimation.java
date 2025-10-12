package com.github.razorplay01.cpa.animation.animations.base;

import com.github.razorplay01.cpa.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.util.enums.AnimationsId;
import com.github.razorplay01.cpa.util.records.AnimationContext;

import static com.github.razorplay01.cpa.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.cpa.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.cpa.util.Util.configureAnimationContainer;

public class TurnRigthAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningStandingAnimationConfig.isEnabled()) {
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
        configureAnimationContainer(CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningStandingAnimationConfig, context.mainAnimationContainer());
        context.mainAnimationContainer().setAnimationSpeed(calculateAnimationSpeed(context));

        context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.TURN_RIGHT_ANIMATION.getAnimationId()));
        context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.TURN_RIGHT_ANIMATION.getAnimationId());
    }

    private static float calculateAnimationSpeed(AnimationContext context) {
        float bodyYawDelta = context.playerData().getBodyYawDelta();
        float halfBodyYawDelta = (float) 1 / 2 * bodyYawDelta;
        float speedMultiplier = CONFIG.getMainAnimations().idleAnimations.turnAnimations.turningStandingAnimationConfig.getSpeedMultiplier();

        if (halfBodyYawDelta > 2 || halfBodyYawDelta < -2) {
            return speedMultiplier;
        }

        return Math.abs(halfBodyYawDelta * speedMultiplier);
    }
}
