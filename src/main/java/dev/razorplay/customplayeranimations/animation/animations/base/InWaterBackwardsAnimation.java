package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class InWaterBackwardsAnimation {
    private InWaterBackwardsAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if ((context.player().isInWaterOrBubble() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming()) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                if (context.playerData().getMovementSpeed() > 0 && context.playerData().isMovingBackwards()) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.inWaterAnimationsConfig.inWaterBackwardsAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_BACKWARDS_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_BACKWARDS_ANIMATION.getAnimationId());
                }
            }
        }
    }
}
