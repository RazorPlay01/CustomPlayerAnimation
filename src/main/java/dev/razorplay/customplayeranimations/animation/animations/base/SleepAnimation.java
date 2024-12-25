package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.util.interfaces.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class SleepAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.extraAnimations.sleepingAnimationsConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            context.overlayAnimationContainer().disableAnimation();
            context.specialAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().disableAnimation();
            context.overlayAnimationContainer().disableAnimation();
            context.specialAnimationContainer().disableAnimation();

            configureAnimationContainer(CONFIG.extraAnimations.sleepingAnimationsConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SLEEP_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.SLEEP_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isSleeping() && !context.player().isPassenger();
    }
}
