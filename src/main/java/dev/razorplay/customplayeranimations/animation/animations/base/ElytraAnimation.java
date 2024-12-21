package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.ELYTRA_ANIMATION;

public class ElytraAnimation {
    private ElytraAnimation() {
        // []
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isFallFlying()) {
            if (!CONFIG.elytraAnimationsConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.elytraAnimationsConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.elytraAnimationsConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.elytraAnimationsConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(ELYTRA_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(ELYTRA_ANIMATION.animationId());
            }
        }
    }
}
