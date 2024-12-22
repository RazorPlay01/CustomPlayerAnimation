package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class InWaterIdleAnimation {
    private InWaterIdleAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if ((context.player().isInWaterOrBubble() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming()) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterIdleAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.inWaterAnimationsConfig.inWaterIdleAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.inWaterAnimationsConfig.inWaterIdleAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.inWaterAnimationsConfig.inWaterIdleAnimationConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_IDLE_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_IDLE_ANIMATION.getAnimationId());
            }
        }
    }
}
