package dev.razorplay.customplayeranimations.animation.animations.base;

import dev.razorplay.customplayeranimations.animation.ICustomAnimation;
import dev.razorplay.customplayeranimations.util.enums.AnimationsId;
import dev.razorplay.customplayeranimations.util.records.AnimationContext;

import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.CONFIG;
import static dev.razorplay.customplayeranimations.CustomPlayerAnimations.getAnimation;
import static dev.razorplay.customplayeranimations.util.Util.configureAnimationContainer;

public class InWaterForwardAnimation implements ICustomAnimation {
    @Override
    public void playAnimation(AnimationContext context) {
        if (shouldPlayAnimation(context)) {
            if (!CONFIG.inWaterAnimationsConfig.inWaterForwardAnimationConfig.isEnabled()) {
                context.mainAnimationContainer().disableAnimation();
            } else {
                configureAnimationContainer(CONFIG.inWaterAnimationsConfig.inWaterForwardAnimationConfig, context.mainAnimationContainer());

                context.mainAnimationContainer().setCurrentAnimation(getAnimation(AnimationsId.IN_WATER_FORWARD_ANIMATION.getAnimationId()));
                context.mainAnimationContainer().setCurrentAnimationId(AnimationsId.IN_WATER_FORWARD_ANIMATION.getAnimationId());
            }
        }
    }

    @Override
    public boolean shouldPlayAnimation(AnimationContext context) {
        return (context.player().isInWaterOrBubble() || context.player().isInLava()) && !context.player().onGround() && !context.player().isVisuallySwimming() && (context.playerData().getMovementSpeed() > 0 && !context.playerData().isMovingBackwards());
    }
}
