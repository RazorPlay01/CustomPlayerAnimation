package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class InWaterForwardAnimation {
    private InWaterForwardAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if ((context.player().isInWaterOrBubble() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming()) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterForwardAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                if (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards()) {
                    context.mainAnimationContainer().setAnimationSpeed(CONFIG.inWaterAnimationsConfig.inWaterForwardAnimationConfig.getSpeedMultiplier());
                    context.mainAnimationContainer().setAnimationFadeTime(CONFIG.inWaterAnimationsConfig.inWaterForwardAnimationConfig.getFadeTime());
                    context.mainAnimationContainer().setAnimationPriority(CONFIG.inWaterAnimationsConfig.inWaterForwardAnimationConfig.getPriority());

                    context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_FORWARD_ANIMATION.getAnimationId()));
                    context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_FORWARD_ANIMATION.getAnimationId());
                }
            }
        }
    }
}
