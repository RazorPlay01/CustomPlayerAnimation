package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class FallAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.extraAnimations.fallingAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                configureAnimationContainer(CONFIG.extraAnimations.fallingAnimationConfig, context.mainAnimationContainer());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.FALLING_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.FALLING_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return context.playerData().getVectorY() < -0.6 && !context.player().isPassenger() && !context.player().onGround() && !context.player().isPassenger();
    }
}
