package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.IDLE_SNEAK_ANIMATION;

public class IdleSneakAnimation {
    private IdleSneakAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isCrouching() && context.playerData().getMovementSpeed() == 0 && !context.playerData().isMovingBackwards() && context.playerData().getBodyYawDelta() == 0) {
            if (!CONFIG.idleSneakAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.idleSneakAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.idleSneakAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.idleSneakAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(IDLE_SNEAK_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(IDLE_SNEAK_ANIMATION.animationId());
            }
        }
    }
}
