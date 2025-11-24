package com.github.razorplay01.cpa.platform.common.animation.animations.base;

import com.github.razorplay01.cpa.platform.common.util.enums.AnimationsId;
import com.github.razorplay01.cpa.platform.common.util.interfaces.ICustomAnimation;
import com.github.razorplay01.cpa.platform.common.util.records.AnimationContext;

import static com.github.razorplay01.cpa.ModTemplate.CONFIG;
import static com.github.razorplay01.cpa.ModTemplate.getAnimation;
import static com.github.razorplay01.cpa.platform.common.util.Util.configureAnimationContainer;

public class SleepAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (!CONFIG.getMainAnimations().extraAnimations.sleepingAnimationsConfig.isEnabled()) {
            context.mainAnimationContainer().disableAnimation();
            context.overlayAnimationContainer().disableAnimation();
            context.specialAnimationContainer().disableAnimation();
        } else {
            context.mainAnimationContainer().disableAnimation();
            context.overlayAnimationContainer().disableAnimation();
            context.specialAnimationContainer().disableAnimation();

            configureAnimationContainer(CONFIG.getMainAnimations().extraAnimations.sleepingAnimationsConfig, context.mainAnimationContainer());

            context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.SLEEP_ANIMATION.getAnimationId()));
            context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.SLEEP_ANIMATION.getAnimationId());
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isSleeping() && !context.player().isPassenger();
    }
}
