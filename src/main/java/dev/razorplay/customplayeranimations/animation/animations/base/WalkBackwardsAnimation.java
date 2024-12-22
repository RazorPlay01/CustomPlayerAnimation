package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class WalkBackwardsAnimation {
    private WalkBackwardsAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards() && !context.player().isCrouching()) {
            if (!CONFIG.walkingBackwardsAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed((float) (context.playerData().getMovementSpeed() * CONFIG.getAnimationMoveSpeedMultiplier() * CONFIG.walkingBackwardsAnimationConfig.getSpeedMultiplier()));
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.walkingBackwardsAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.walkingBackwardsAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.WALKING_BACKWARDS_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.WALKING_BACKWARDS_ANIMATION.getAnimationId());
            }
        }
    }
}
