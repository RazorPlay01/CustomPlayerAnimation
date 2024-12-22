package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class InWaterSwimAnimation {
    private InWaterSwimAnimation() {
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

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_SWIMMING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_SWIMMING_ANIMATION.getAnimationId());
            }
        }
    }
}
