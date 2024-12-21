package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.IN_WATER_UP_ANIMATION;

public class InWaterUpAnimation {
    private InWaterUpAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if ((context.player().isInWaterOrBubble() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming()) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterUpAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.inWaterAnimationsConfig.inWaterUpAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.inWaterAnimationsConfig.inWaterUpAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.inWaterAnimationsConfig.inWaterUpAnimationConfig.getPriority());

                if (context.playerData().getVectorY() > 0) {
                    context.mainAnimationContainer().setCurrentAnimation(IN_WATER_UP_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(IN_WATER_UP_ANIMATION.animationId());
                }
            }
        }
    }
}
