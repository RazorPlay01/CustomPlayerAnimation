package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.*;

public class WalkSneakAnimation {
    private WalkSneakAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards() && context.player().isCrouching()) {
            if (!CONFIG.walkingSneakAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (5 * context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.walkingSneakAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.walkingSneakAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.walkingSneakAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(WALKING_SNEAK_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(WALKING_SNEAK_ANIMATION.animationId());
            }
        }
    }
}
