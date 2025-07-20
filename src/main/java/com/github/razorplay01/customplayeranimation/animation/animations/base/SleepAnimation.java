package com.github.razorplay01.customplayeranimation.animation.animations.base;

import com.github.razorplay01.customplayeranimation.util.interfaces.ICustomAnimation;
import com.github.razorplay01.customplayeranimation.util.enums.AnimationsId;
import com.github.razorplay01.customplayeranimation.util.records.AnimationContext;

import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.CONFIG;
import static com.github.razorplay01.customplayeranimation.CustomPlayerAnimations.getAnimation;
import static com.github.razorplay01.customplayeranimation.util.Util.configureAnimationContainer;

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
