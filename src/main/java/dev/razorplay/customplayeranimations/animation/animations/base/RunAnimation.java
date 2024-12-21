package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.RUNNING_ANIMATION;

public class RunAnimation {
    private RunAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && context.player().isSprinting() && !context.playerData().isMovingBackwards() && !context.player().isCrouching()) {
            if (!CONFIG.runningAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.runningAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.runningAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.runningAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(RUNNING_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(RUNNING_ANIMATION.animationId());
            }
        }
    }
}
