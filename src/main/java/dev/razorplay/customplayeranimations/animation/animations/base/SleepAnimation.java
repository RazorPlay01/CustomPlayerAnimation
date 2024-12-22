package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;

public class SleepAnimation {
    private SleepAnimation() {
        //[]
    }

    public static void playAnimation(AnimationContext context) {
        if (context.player().isSleeping()) {
            if (!CONFIG.sleepingAnimationsConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
                context.overlayAnimationContainer().disableAnimation();
                context.upHandAnimationContainer().disableAnimation();
            } else {
                context.mainAnimationContainer().disableAnimation();
                context.overlayAnimationContainer().disableAnimation();
                context.upHandAnimationContainer().disableAnimation();

                context.mainAnimationContainer().setAnimationSpeed(CONFIG.sleepingAnimationsConfig.getSpeedMultiplier());
                context.mainAnimationContainer().setAnimationFadeTime(CONFIG.sleepingAnimationsConfig.getFadeTime());
                context.mainAnimationContainer().setAnimationPriority(CONFIG.sleepingAnimationsConfig.getPriority());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SLEEPING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.SLEEPING_ANIMATION.getAnimationId());
            }
        }
    }
}
