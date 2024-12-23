package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

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

                configureAnimationContainer(CONFIG.sleepingAnimationsConfig, context.mainAnimationContainer());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SLEEPING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.SLEEPING_ANIMATION.getAnimationId());
            }
        }
    }
}
