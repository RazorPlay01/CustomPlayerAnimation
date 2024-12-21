package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.FALLING_ANIMATION;

public class FallAnimation {
    private FallAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.playerData().getVectorY() < -0.6 && !context.player().isPassenger() && !context.player().onGround()) {
            if (!CONFIG.fallingAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.fallingAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.fallingAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.fallingAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(FALLING_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(FALLING_ANIMATION.animationId());
            }
        }
    }
}
