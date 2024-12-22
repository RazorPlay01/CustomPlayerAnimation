package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

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

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.RUNNING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.RUNNING_ANIMATION.getAnimationId());
            }
        }
    }
}
