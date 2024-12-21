package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.IN_WATER_SWIMMING_ANIMATION;

public class InWaterSwimAnimations {
    private InWaterSwimAnimations() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isInWaterOrBubble() && context.player().isVisuallySwimming()) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterSwimAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.inWaterAnimationsConfig.inWaterSwimAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.inWaterAnimationsConfig.inWaterSwimAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.inWaterAnimationsConfig.inWaterSwimAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(IN_WATER_SWIMMING_ANIMATION.animation());
                context.mainAnimationContainer().setCurrentAnimationId(IN_WATER_SWIMMING_ANIMATION.animationId());
            }
        }
    }
}
