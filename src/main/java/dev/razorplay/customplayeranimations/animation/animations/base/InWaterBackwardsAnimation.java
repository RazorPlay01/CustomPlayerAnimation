package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.animation.AnimationProvider.IN_WATER_BACKWARDS_ANIMATION;

public class InWaterBackwardsAnimation {
    private InWaterBackwardsAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if ((context.player().isInWaterOrBubble() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming()) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().setAnimationSpeed(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.getPriority());

                if (context.playerData().getMovementSpeed() > 0) {
                    context.mainAnimationContainer().setCurrentAnimation(IN_WATER_BACKWARDS_ANIMATION.animation());
                    context.mainAnimationContainer().setCurrentAnimationId(IN_WATER_BACKWARDS_ANIMATION.animationId());
                }
            }
        }
    }
}
