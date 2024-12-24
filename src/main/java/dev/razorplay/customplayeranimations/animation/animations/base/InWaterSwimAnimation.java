package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class InWaterSwimAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterSwimAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                configureAnimationContainer(CONFIG.inWaterAnimationsConfig.inWaterSwimAnimationConfig, context.mainAnimationContainer());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_SWIMMING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_SWIMMING_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.player().isInWaterOrBubble() && context.player().isVisuallySwimming();
    }
}
