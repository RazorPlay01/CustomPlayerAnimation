package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.WALKING_SNEAK_ANIMATION;
import static java.lang.Math.abs;

public class TurnSneakAnimation {
    private TurnSneakAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isCrouching() && context.playerData().getMovementSpeed() == 0 && !context.playerData().isMovingBackwards() && context.playerData().getBodyYawDelta() != 0) {
            if (!CONFIG.turningSneakAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                if ((((float) 1 / 2) * context.playerData().getBodyYawDelta()) > 1.5 || (((float) 1 / 2) * context.playerData().getBodyYawDelta()) < -1.5) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.turningSneakAnimationConfig.getSpeedMultiplier());
                } else {
                    context.mainAnimationContainer().setAnimationSpeed(abs((((float) 1 / 2) * context.playerData().getBodyYawDelta()) * CONFIG.turningSneakAnimationConfig.getSpeedMultiplier()));
                }
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.turningSneakAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.turningSneakAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(WALKING_SNEAK_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(WALKING_SNEAK_ANIMATION.animationId());
            }
        }
    }
}
