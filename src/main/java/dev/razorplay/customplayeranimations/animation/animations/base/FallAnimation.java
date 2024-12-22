package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

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

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.FALLING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.FALLING_ANIMATION.getAnimationId());
            }
        }
    }
}
