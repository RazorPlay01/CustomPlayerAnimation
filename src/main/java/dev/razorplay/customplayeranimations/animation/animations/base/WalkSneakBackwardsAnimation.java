package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.WALKING_SNEAK_BACKWARDS_ANIMATION;

public class WalkSneakBackwardsAnimation {
    private WalkSneakBackwardsAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards() && context.player().isCrouching()) {
            if (!CONFIG.walkingSneakBackwardsAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (5 * context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.walkingSneakBackwardsAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.walkingSneakBackwardsAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.walkingSneakBackwardsAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(WALKING_SNEAK_BACKWARDS_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(WALKING_SNEAK_BACKWARDS_ANIMATION.animationId());
            }
        }
    }
}
